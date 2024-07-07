package io.github.kpermissions.handler.permissions

import android.Manifest
import androidx.core.app.ActivityCompat
import io.github.kpermissions.handler.PermissionHandler
import io.github.kpermissions.handler.permissionRequest


fun requestReadContactsPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val permission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.READ_CONTACTS
    )
    permissionRequest(permission, onPermissionResult)

}

fun requestWriteContactsPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val permission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_CONTACTS
    )
    permissionRequest(permission, onPermissionResult)

}