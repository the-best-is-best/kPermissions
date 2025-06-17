package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.isDeclaredInManifest
import io.github.kPermissions_api.refreshState
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
    var listenToStatus by remember { mutableStateOf(true) }
    var isServiceActive by remember { mutableStateOf(true) }

    val accStatus = launcher.status

    LaunchedEffect(accStatus) {
        listenToStatus = true
        isServiceActive =
            if (accStatus is com.google.accompanist.permissions.PermissionStatus.Granted) {
                permission.isServiceAvailable()
            } else {
                true
            }
        onPermissionResult(accStatus is com.google.accompanist.permissions.PermissionStatus.Granted && isServiceActive)
    }

    var currentStatus by remember { mutableStateOf<PermissionStatus?>(null) }

    return object : PermissionState {
        override val permission = permission
        override val status: PermissionStatus
            get() {
                if (listenToStatus) {
                    if (!permission.isDeclaredInManifest()) return PermissionStatus.NotDeclared
                    val status = when (accStatus) {
                        com.google.accompanist.permissions.PermissionStatus.Granted -> {
                            if (isServiceActive) PermissionStatus.Granted else PermissionStatus.Unavailable
                        }
                        is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                            if (shouldRequest || accStatus.shouldShowRationale) {
                                PermissionStatus.Denied
                            } else {
                                PermissionStatus.DeniedPermanently
                            }
                        }
                    }
                    currentStatus = status
                }
                return currentStatus ?: PermissionStatus.Denied
            }

        override fun launchPermissionRequest() {
            listenToStatus = false
            launcher.launchPermissionRequest()
            shouldRequest = false
        }

        override fun openAppSettings() = openAppSettingsPlatform()

        override suspend fun refreshStatus() {
            permission.refreshState()
            if (launcher.status is com.google.accompanist.permissions.PermissionStatus.Granted) {
                isServiceActive = permission.isServiceAvailable()
            }
        }
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

    // تحديث حالة الإذن لجميع الصلاحيات المراقبة
    LaunchedEffect(multipleLauncher.permissions.map { it.status }) {
        snapshotFlow { multipleLauncher.permissions.map { it.status } }
            .collect { statusList ->
                val mappedStatuses = statusList.mapIndexed { index, accStatus ->
                    if (accStatus is com.google.accompanist.permissions.PermissionStatus.Granted) {
                        // نتحقق من تفعيل الخدمة لكل صلاحية ممنوحة
                        val perm = filteredPermissions.getOrNull(index)
                        if (perm != null) {
                            val isActive = perm.isServiceAvailable()
                            if (isActive) PermissionStatus.Granted else PermissionStatus.Unavailable
                        } else {
                            PermissionStatus.Denied // أو اختيار منطقي آخر
                        }
                    } else {
                        when (accStatus) {
                            is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                                if (accStatus.shouldShowRationale) PermissionStatus.Denied else PermissionStatus.DeniedPermanently
                            }

                            else -> PermissionStatus.Denied
                        }
                    }
                }
                onPermissionsResult(mappedStatuses.all { it == PermissionStatus.Granted })
            }
    }

    return filteredPermissions.mapIndexed { index, perm ->

        var shouldRequest by remember { mutableStateOf(true) }
        var listenToStatus by remember { mutableStateOf(true) }
        var currentStatus by remember { mutableStateOf<PermissionStatus?>(null) }
        var isServiceActive by remember { mutableStateOf(true) } // متغير حالة الخدمة

        val accStatus = multipleLauncher.permissions.getOrNull(index)?.status

        // عندما تتغير حالة الإذن، نتحقق من حالة الخدمة إذا كانت ممنوحة
        LaunchedEffect(accStatus) {
            listenToStatus = true
            if (accStatus is com.google.accompanist.permissions.PermissionStatus.Granted) {
                isServiceActive = perm.isServiceAvailable()
            } else {
                isServiceActive = true // افتراضي في حالة عدم السماح
            }
        }

        object : PermissionState {
            override val permission = perm

            override val status: PermissionStatus
                get() {
                    if (!perm.isDeclaredInManifest()) return PermissionStatus.NotDeclared

                    val accStatus = multipleLauncher.permissions.getOrNull(index)?.status
                        ?: return PermissionStatus.Denied

                    if (listenToStatus) {
                        val status = when (accStatus) {
                            is com.google.accompanist.permissions.PermissionStatus.Granted -> {
                                if (isServiceActive) PermissionStatus.Granted else PermissionStatus.Unavailable
                            }
                            is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                                if (shouldRequest || accStatus.shouldShowRationale) {
                                    PermissionStatus.Denied
                                } else {
                                    PermissionStatus.DeniedPermanently
                                }
                            }
                        }
                        currentStatus = status
                    }

                    return currentStatus ?: PermissionStatus.Denied
                }

            override fun launchPermissionRequest() {
                listenToStatus = false
                shouldRequest = false
                multipleLauncher.launchMultiplePermissionRequest()
            }

            override fun openAppSettings() = openAppSettingsPlatform()
            override suspend fun refreshStatus() {
                perm.refreshState()
                if (multipleLauncher.permissions.getOrNull(index)?.status is com.google.accompanist.permissions.PermissionStatus.Granted) {
                    isServiceActive = perm.isServiceAvailable()
                }
            }
        }
    } + (permissions - filteredPermissions.toSet()).map {
        object : PermissionState {
            override val permission = it
            override val status = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() = openAppSettingsPlatform()
            override suspend fun refreshStatus() {
                it.refreshState()
            }
        }
    }
}


@Composable
private fun grantedState(permission: Permission) = object : PermissionState {
    override val permission = permission
    override val status = PermissionStatus.Granted
    override fun launchPermissionRequest() {}
    override fun openAppSettings() = openAppSettingsPlatform()
    override suspend fun refreshStatus() {
        permission.refreshState()
    }
}

