package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest

suspend fun requestReadCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.READ_CALENDAR

    permissionRequest(arrayOf(permission), onPermissionResult)
}

suspend fun requestWriteCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.WRITE_CALENDAR

    permissionRequest(arrayOf(permission), onPermissionResult)

}