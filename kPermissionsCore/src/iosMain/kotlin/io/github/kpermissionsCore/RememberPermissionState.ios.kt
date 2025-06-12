package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    if (permission.ignore == PlatformIgnore.IOS) {
        onPermissionResult(true)
        return object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }

    fun getStatus() = PermissionStatusRegistry.getStatus(permission.name)

    var status by remember { mutableStateOf(getStatus()) }

    LaunchedEffect(Unit) {
        onPermissionResult(getStatus() == PermissionStatus.Granted)
    }
    OnAppResumed {
        val newStatus = getStatus()
        if (newStatus != status) {
            status = newStatus
            onPermissionResult(getStatus() == PermissionStatus.Granted)
        }
    }



    return object : PermissionState {
        override val permission: Permission = permission


        override var status: PermissionStatus = getStatus()

        override fun launchPermissionRequest() {
            permission.permissionRequest { granted ->
                status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                onPermissionResult(granted)
            }
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }
}