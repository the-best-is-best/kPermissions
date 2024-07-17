package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest


suspend fun requestReadContactsPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.READ_CONTACTS

    permissionRequest(arrayOf(permission), onPermissionResult)

}

suspend fun requestWriteContactsPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.WRITE_CONTACTS

    permissionRequest(arrayOf(permission), onPermissionResult)

}