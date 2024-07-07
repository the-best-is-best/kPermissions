package io.github.kpermissions.handler

import android.content.pm.PackageManager

fun permissionRequest(permission: Int, onPermissionResult: (Boolean) -> Unit) {
    if (permission == PackageManager.PERMISSION_GRANTED) {
        onPermissionResult(true)
    } else if (permission == PackageManager.PERMISSION_DENIED) {
        if (PermissionHandler.getCanOpenSetting()) {
            openAppSettings()
        }
        onPermissionResult(false)
    }
}