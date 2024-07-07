package io.github.kpermissions.handler.permissions

import android.Manifest
import androidx.core.app.ActivityCompat
import io.github.kpermissions.handler.PermissionHandler
import io.github.kpermissions.handler.permissionRequest

fun requestReadCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val permission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.READ_CALENDAR
    )
    permissionRequest(permission, onPermissionResult)
}

fun requestWriteCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val permission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_CALENDAR
    )
    permissionRequest(permission, onPermissionResult)

}