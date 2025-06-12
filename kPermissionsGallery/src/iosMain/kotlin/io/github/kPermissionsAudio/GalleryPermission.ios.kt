package io.github.kPermissionsAudio

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PermissionStatusRegistry
import io.github.kpermissionsCore.PlatformIgnore
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary

private fun getGalleryPermissionStatus(): PermissionStatus {
    return when (PHPhotoLibrary.authorizationStatus()) {
        PHAuthorizationStatusAuthorized -> PermissionStatus.Granted
        PHAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
        PHAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
        PHAuthorizationStatusNotDetermined -> PermissionStatus.Denied
        PHAuthorizationStatusLimited -> PermissionStatus.Granted // iOS 14+
        else -> PermissionStatus.Denied
    }
}


actual object GalleryPermission : Permission {
    init {
        PermissionStatusRegistry.register("gallery") {
            getGalleryPermissionStatus()
        }
    }

    actual override val name: String
        get() = "gallery"

    actual override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            PHPhotoLibrary.requestAuthorization { status ->
                when (status) {
                    PHAuthorizationStatusAuthorized,
                    PHAuthorizationStatusLimited -> callback(true)

                    else -> callback(false)
                }
            }
        }

    override val type: io.github.kpermissionsCore.PermissionType
        get() = io.github.kpermissionsCore.PermissionType.Gallery

    actual override var ignore: PlatformIgnore = PlatformIgnore.None

}