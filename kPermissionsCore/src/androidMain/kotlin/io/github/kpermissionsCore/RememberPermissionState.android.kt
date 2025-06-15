package io.github.kpermissionsCore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.github.kPermissions_api.AndroidPermission
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.isDeclaredInManifest
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.getIgnore
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    val androidPermission = permission.androidPermissionName
    val currentSdk = android.os.Build.VERSION.SDK_INT
    val context = AndroidPermission.getActivity() ?: LocalContext.current

    // تخطي إذا الإذن غير مطلوب أو خارجه من SDK range
    if (androidPermission == null ||
        permission.getIgnore() == PlatformIgnore.Android ||
        (permission.minSdk != null && currentSdk < permission.minSdk!!) ||
        (permission.maxSdk != null && currentSdk > permission.maxSdk!!)
    ) {
        LaunchedEffect(Unit) { onPermissionResult(true) }
        return grantedState(permission)
    }

    val launcher = rememberPermissionState(androidPermission)
    val sharedPrefs = remember {
        context.getSharedPreferences("kPermissions_cmp", Context.MODE_PRIVATE)
    }
    var shouldRequest by remember { mutableStateOf(false) }

    LaunchedEffect(launcher.status, shouldRequest) {
        if (shouldRequest) {
            shouldRequest = false
        }
        val granted = launcher.status == com.google.accompanist.permissions.PermissionStatus.Granted
        onPermissionResult(granted)
    }

    return object : PermissionState {
        override val permission = permission
        override val status: PermissionStatus
            get() {
                if (!permission.isDeclaredInManifest()) return PermissionStatus.NotDeclared

                val accStatus = launcher.status
                val alreadyRequested = sharedPrefs.getBoolean("requested_${permission.name}", false)

                return when (accStatus) {
                    com.google.accompanist.permissions.PermissionStatus.Granted -> PermissionStatus.Granted
                    is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                        if (accStatus.shouldShowRationale || !alreadyRequested) {
                            PermissionStatus.Denied
                        } else {
                            PermissionStatus.DeniedPermanently
                        }
                    }
                }
            }

        override fun launchPermissionRequest() {
            sharedPrefs.edit {
                putBoolean("requested_${permission.name}", true)
            }
            launcher.launchPermissionRequest()
            shouldRequest = true
        }

        override fun openAppSettings() = openAppSettingsPlatform()
        override fun checkPermissionStatus() = status
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit
): List<PermissionState> {
    val currentSdk = android.os.Build.VERSION.SDK_INT
    val context = AndroidPermission.getActivity() ?: LocalContext.current

    val filteredPermissions = remember(permissions) {
        permissions.filter {
            it.getIgnore() != PlatformIgnore.Android &&
                    it.androidPermissionName != null &&
                    (it.minSdk?.let { sdk -> currentSdk >= sdk } ?: true) &&
                    (it.maxSdk?.let { sdk -> currentSdk <= sdk } ?: true)
        }
    }

    val androidPermissions = filteredPermissions.mapNotNull { it.androidPermissionName }
    val multipleLauncher = rememberMultiplePermissionsState(androidPermissions)

    val sharedPrefs = remember {
        context.getSharedPreferences("kPermissions_cmp", Context.MODE_PRIVATE)
    }

    LaunchedEffect(multipleLauncher.permissions) {
        val allGranted =
            multipleLauncher.permissions.all { it.status == com.google.accompanist.permissions.PermissionStatus.Granted }
        onPermissionsResult(allGranted)
    }

    return filteredPermissions.mapIndexed { index, perm ->
        object : PermissionState {
            override val permission = perm
            override val status: PermissionStatus
                get() {
                    if (!perm.isDeclaredInManifest()) return PermissionStatus.NotDeclared

                    val accStatus = multipleLauncher.permissions.getOrNull(index)?.status
                    val alreadyRequested = sharedPrefs.getBoolean("requested_${perm.name}", false)

                    return when (accStatus) {
                        com.google.accompanist.permissions.PermissionStatus.Granted -> PermissionStatus.Granted
                        is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                            if (accStatus.shouldShowRationale || !alreadyRequested) {
                                PermissionStatus.Denied
                            } else {
                                PermissionStatus.DeniedPermanently
                            }
                        }

                        else -> PermissionStatus.Denied
                    }
                }

            override fun launchPermissionRequest() {
                sharedPrefs.edit {
                    putBoolean("requested_${perm.name}", true)
                }
                multipleLauncher.launchMultiplePermissionRequest()
            }

            override fun openAppSettings() = openAppSettingsPlatform()
            override fun checkPermissionStatus() = status
        }
    } + (permissions - filteredPermissions.toSet()).map {
        object : PermissionState {
            override val permission = it
            override val status = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() = openAppSettingsPlatform()
            override fun checkPermissionStatus() = PermissionStatus.Granted
        }
    }
}

@Composable
private fun grantedState(permission: Permission) = object : PermissionState {
    override val permission = permission
    override val status = PermissionStatus.Granted
    override fun launchPermissionRequest() {}
    override fun openAppSettings() = openAppSettingsPlatform()
    override fun checkPermissionStatus() = PermissionStatus.Granted
}
