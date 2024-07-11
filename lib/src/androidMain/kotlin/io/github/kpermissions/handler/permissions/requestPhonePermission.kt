package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest

fun requestPhonePermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.CALL_PHONE

    permissionRequest(arrayOf(permission), onPermissionResult)

}