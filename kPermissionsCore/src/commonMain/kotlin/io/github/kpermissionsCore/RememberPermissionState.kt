package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import io.github.kPermissions_api.MultiPermissionState
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus

@Composable
internal expect fun RequestPermission(
    permission: Permission,
): PermissionState


@Composable
internal expect fun RequestMultiPermissions(
    permissions: List<Permission>,
): MultiPermissionState

@Composable
fun rememberPermissionState(
    permission: Permission,
): PermissionState {
    val isInspection = LocalInspectionMode.current

    return if (isInspection)
        rememberPreviewPermissionState(permission)
    else
        RequestPermission(permission)
}

@Composable
fun rememberMultiplePermissionsState(
    permissions: List<Permission>,
): MultiPermissionState {
    val isInspection = LocalInspectionMode.current

    return if (isInspection)
        rememberPreviewMultiPermissionState(permissions)
    else
        RequestMultiPermissions(permissions)
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


@Composable
private fun rememberPreviewMultiPermissionState(
    permissions: List<Permission>,
): MultiPermissionState = remember {
    object : MultiPermissionState {
        override val permissions: List<Permission>
            get() = permissions
        override val statuses: List<PermissionStatus>
            get() = listOf(PermissionStatus.Granted)

        override fun launchPermissionsRequest() {
        }

        override fun openAppSettings() {

        }

        override suspend fun refreshStatuses() {
        }

    }

}

