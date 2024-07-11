package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest

fun requestCameraPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.CAMERA

    permissionRequest(arrayOf(permission), onPermissionResult)

}