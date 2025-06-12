package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode

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

/**
 * ⚠️ Use with caution: Requesting multiple permissions (like CAMERA and STORAGE) at once
 * may silently fail on Android 9 and below.
 *
 * 👉 To avoid this, register such permissions with `ignore = PlatformIgnore.Android` in register
 * or request them separately.
 *
 * This function does NOT filter platform-specific limitations — the developer is responsible.
 */
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
    }
}


