package io.github.kpermissions.handler.permissions

import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary


fun requestPhotoPermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}

fun requestVideoPermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}

fun requestGalleryPermission(onPermissionResult: (Boolean) -> Unit) {
    val status = PHPhotoLibrary.authorizationStatus()
    when (status) {
        PHAuthorizationStatusAuthorized -> {
            onPermissionResult(true)
        }

        PHAuthorizationStatusNotDetermined -> {
            PHPhotoLibrary.requestAuthorization { newStatus ->
                onPermissionResult(newStatus == PHAuthorizationStatusAuthorized)
            }
        }

        else -> {
            onPermissionResult(false)
        }
    }
}
