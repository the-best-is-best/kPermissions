package io.github.kpermissionsCamera

import android.Manifest
import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

@Composable
internal actual fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = Manifest.permission.CAMERA,
        onResult = onResult,

    )
}

actual fun CameraPermission.register(ignore: PlatformIgnore) {
    if (isCameraPermissionRegistered) return
    isCameraPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        CameraPermission::class
    ) { permission, onResult ->
        CameraPermissionState(permission as CameraPermission, onResult)
    }
}