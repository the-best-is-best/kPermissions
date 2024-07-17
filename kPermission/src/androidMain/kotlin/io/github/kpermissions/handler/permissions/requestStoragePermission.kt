package io.github.kpermissions.handler.permissions

import android.Manifest
import android.os.Build
import io.github.kpermissions.handler.permissionRequest

suspend fun requestReadStoragePermission(onPermissionResult: (Boolean) -> Unit) {
    if (32 >= Build.VERSION.SDK_INT) {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        permissionRequest(arrayOf(permission), onPermissionResult)
    } else {
        onPermissionResult(true)
    }
}


suspend fun requestWriteStoragePermission(onPermissionResult: (Boolean) -> Unit) {
    if (32 >= Build.VERSION.SDK_INT) {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        permissionRequest(arrayOf(permission), onPermissionResult)
    } else {
        onPermissionResult(true)
    }
}