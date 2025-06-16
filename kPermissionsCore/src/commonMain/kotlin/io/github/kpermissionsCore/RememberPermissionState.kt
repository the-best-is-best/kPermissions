package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus

@Composable
internal expect fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit,
): PermissionState


@Composable
internal expect fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit,
): List<PermissionState>

@Composable
fun rememberPermissionState(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit,
): PermissionState {
    val isInspection = LocalInspectionMode.current

    return if (isInspection)
        rememberPreviewPermissionState(permission)
    else
        RequestPermission(permission, onPermissionResult)
}

@Composable
fun rememberMultiplePermissionsState(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit,
): List<PermissionState> {
    val isInspection = LocalInspectionMode.current

    return if (isInspection)
        permissions.map { rememberPreviewPermissionState(it) }
    else
        RequestMultiPermissions(permissions, onPermissionsResult)
}


@Composable
private fun rememberPreviewPermissionState(
    permission: Permission,
): PermissionState = remember {
    object : PermissionState {
        override val permission: Permission = permission
        override var status: PermissionStatus = PermissionStatus.Granted
        override fun launchPermissionRequest() {}
        override fun openAppSettings() {}
        override suspend fun refreshStatus() {

        }


    }
}


