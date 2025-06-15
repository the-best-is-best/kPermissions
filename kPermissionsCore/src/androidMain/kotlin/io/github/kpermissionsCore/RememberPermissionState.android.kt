package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
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

    if (androidPermission == null ||
        permission.getIgnore() == PlatformIgnore.Android ||
        (permission.minSdk != null && currentSdk < permission.minSdk!!) ||
        (permission.maxSdk != null && currentSdk > permission.maxSdk!!)
    ) {
        LaunchedEffect(Unit) { onPermissionResult(true) }
        return grantedState(permission)
    }

    val launcher = rememberPermissionState(androidPermission)

    var shouldRequest by remember { mutableStateOf(true) }


    val accStatus = launcher.status


    return object : PermissionState {
        override val permission = permission
        override val status: PermissionStatus
            get() {
                if (!permission.isDeclaredInManifest()) return PermissionStatus.NotDeclared


                return when (accStatus) {
                    com.google.accompanist.permissions.PermissionStatus.Granted -> PermissionStatus.Granted
                    is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                        if (shouldRequest || accStatus.shouldShowRationale) {
                            PermissionStatus.Denied
                        } else {
                            PermissionStatus.DeniedPermanently
                        }
                    }
                }
            }

        override fun launchPermissionRequest() {

            launcher.launchPermissionRequest()
            shouldRequest = false
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

    val shouldRequestList = remember { mutableStateListOf<Boolean>() }

    LaunchedEffect(filteredPermissions) {
        if (shouldRequestList.size != filteredPermissions.size) {
            shouldRequestList.clear()
            shouldRequestList.addAll(List(filteredPermissions.size) { true })
        }
    }

    LaunchedEffect(multipleLauncher.permissions) {
        val allGranted = multipleLauncher.permissions.all {
            it.status == com.google.accompanist.permissions.PermissionStatus.Granted
        }
        onPermissionsResult(allGranted)
    }

    return filteredPermissions.mapIndexed { index, perm ->
        object : PermissionState {
            override val permission = perm

            override val status: PermissionStatus
                get() {
                    if (!perm.isDeclaredInManifest()) return PermissionStatus.NotDeclared

                    val accStatus = multipleLauncher.permissions.getOrNull(index)?.status
                        ?: return PermissionStatus.Denied

                    return when (accStatus) {
                        is com.google.accompanist.permissions.PermissionStatus.Granted -> PermissionStatus.Granted
                        is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                            if (shouldRequestList.getOrNull(index) == true || accStatus.shouldShowRationale) {
                                PermissionStatus.Denied
                            } else {
                                PermissionStatus.DeniedPermanently
                            }
                        }
                    }
                }

            override fun launchPermissionRequest() {
                multipleLauncher.launchMultiplePermissionRequest()
                if (index in shouldRequestList.indices) {
                    shouldRequestList[index] = false
                }
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
