package io.github.kPermissionsGallery

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore
import kotlin.experimental.ExperimentalObjCName

@Composable
internal actual fun GalleryPermissionState(
    permission: GalleryPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    TODO("Not yet implemented")
}

private fun getCameraPermissionStatus(): io.github.kpermissionsCore.PermissionStatus {

}

@OptIn(ExperimentalObjCName::class)
@ObjCName("registerGalleryPermission")
actual fun GalleryPermission.register(ignore: PlatformIgnore) {
    if (isGalleryPermissionRegistered) return
    isGalleryPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        GalleryPermission::class
    ) { permission, onResult ->
        io.github.kpermissionsCore.PermissionStatusRegistry.register(permission.name) {
            getCameraPermissionStatus()
        }
        GalleryPermissionState(permission as GalleryPermission, onResult)

    }

}