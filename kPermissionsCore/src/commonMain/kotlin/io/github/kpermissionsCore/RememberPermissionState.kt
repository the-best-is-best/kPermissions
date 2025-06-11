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

@Composable
fun rememberMultiplePermissionsState(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit,
): List<PermissionState> {
    val isInspection = LocalInspectionMode.current

    val states = permissions.map { permission ->
        if (isInspection) {
            rememberPreviewPermissionState(permission)
        } else {
            permissionStateHolder(permission) {}
        }
    }

    LaunchedEffect(states.map { it.status }) {
        snapshotFlow {
            states.all { it.status.isGranted }
        }.collect { allGranted ->
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


