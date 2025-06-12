package io.github.kPermissionsAudio

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

@Composable
internal actual fun GalleryPermissionState(
    permission: GalleryPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else null,
        onResult = onResult
    )
}

actual fun GalleryPermission.register(ignore: PlatformIgnore) {

    if (isGalleryPermissionRegistered) return
    isGalleryPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        GalleryPermission::class
    ) { permission, onResult ->
        GalleryPermissionState(permission as GalleryPermission, onResult)
    }
}