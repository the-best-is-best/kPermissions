package io.github.kPermissionsGallery

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
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

internal fun permissionRequest(): ((Boolean) -> Unit) -> Unit {
    return { callback ->
        val status = PHPhotoLibrary.authorizationStatus()

        if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited) {
            callback(true)
        } else {
            PHPhotoLibrary.requestAuthorization { newStatus ->
                when (newStatus) {
                    PHAuthorizationStatusAuthorized, PHAuthorizationStatusLimited -> callback(true)
                    else -> callback(false)
                }
            }
        }
    }
}



actual object GalleryPermission : Permission {

    override val name: String
        get() = "gallery"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = permissionRequest()

    override suspend fun isServiceAvailable(): Boolean {
        return true
    }

    override suspend fun getPermissionStatus(): PermissionStatus {
        return getGalleryPermissionStatus()
    }
    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null

    override val minSdk: Int?
        get() = _minSdk

    override val maxSdk: Int?
        get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }


}