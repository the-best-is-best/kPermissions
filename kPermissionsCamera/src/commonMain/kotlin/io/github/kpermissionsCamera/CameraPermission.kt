package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState

object CameraPermission : Permission {
    override val name: String
        get() = "camera"
}

@Composable
internal expect fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isCameraPermissionRegistered = false


expect fun CameraPermission.register()

