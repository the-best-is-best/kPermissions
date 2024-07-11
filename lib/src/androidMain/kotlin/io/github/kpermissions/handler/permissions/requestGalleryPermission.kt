package io.github.kpermissions.handler.permissions

import android.Manifest
import android.os.Build
import io.github.kpermissions.handler.permissionRequest

fun requestPhotoPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        null
    }
    if (permission != null) {
        permissionRequest(arrayOf(permission), onPermissionResult)

    } else {
        onPermissionResult(true)
    }
}

fun requestVideoPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        null
    }
    if (permission != null) {
        permissionRequest(arrayOf(permission), onPermissionResult)

    } else {
        onPermissionResult(true)
    }
}

fun requestGalleryPermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}