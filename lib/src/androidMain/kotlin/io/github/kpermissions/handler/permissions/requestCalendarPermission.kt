package io.github.kpermissions.handler.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import io.github.kpermissions.handler.PermissionHandler

fun requestCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = PermissionHandler.getAppContext()
    val permission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.READ_CALENDAR
    )
    if (permission == PackageManager.PERMISSION_GRANTED) {
        onPermissionResult(true)
    } else if (permission == PackageManager.PERMISSION_DENIED) {
        onPermissionResult(false)
    }
}