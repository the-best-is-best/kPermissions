package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalInspectionMode


@Composable
fun rememberPermissionState(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit,
): PermissionState {
    val isInspection = LocalInspectionMode.current

    return if (isInspection)
        rememberPreviewPermissionState(permission)
    else
        permissionStateHolder(permission, onPermissionResult)
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

    val states = permissions.map { permission ->
        rememberPermissionState(permission) {}
    }

    LaunchedEffect(states) {
        snapshotFlow {
            states.map { it.status }
        }.collect { currentStatuses ->
            val allGranted = currentStatuses.all { it.isGranted }
            onPermissionsResult(allGranted)
        }
    }

    return states
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


