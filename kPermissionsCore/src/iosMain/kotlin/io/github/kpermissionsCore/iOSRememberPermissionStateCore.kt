package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun IOSRememberPermissionStateCore(
    permission: Permission,
    permissionRequest: ((Boolean) -> Unit) -> Unit,
    getStatus: () -> PermissionStatus = { PermissionStatusRegistry.getStatus(permission.name) },
    onResult: (Boolean) -> Unit,
): PermissionState {

    var status by remember { mutableStateOf(getStatus()) }

    LaunchedEffect(Unit) {
        onResult(getStatus() == PermissionStatus.Granted)
    }
    OnAppResumed {
        val newStatus = getStatus()
        if (newStatus != status) {
            status = newStatus
            onResult(getStatus() == PermissionStatus.Granted)
        }
    }



    return object : PermissionState {
        override val permission: Permission = permission


        override var status: PermissionStatus
            get() = status
            set(_) {}

        override fun launchPermissionRequest() {
            permissionRequest { granted ->
                status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                onResult(granted)
            }
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }
}
