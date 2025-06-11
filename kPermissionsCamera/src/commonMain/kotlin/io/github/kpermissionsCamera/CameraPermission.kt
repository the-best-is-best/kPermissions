package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState

data object CameraPermission : Permission

@Composable
internal expect fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

private var isCameraPermissionRegistered = false

fun CameraPermission.register() {
    if (isCameraPermissionRegistered) return
    isCameraPermissionRegistered = true

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        CameraPermission::class
    ) { permission, onResult ->
        CameraPermissionState(permission as CameraPermission, onResult)
    }
}
