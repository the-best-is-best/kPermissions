package io.github.kpermissions.handler.permissions

import android.Manifest
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import io.github.kpermissions.handler.PermissionHandler
import io.github.kpermissions.handler.permissionRequest

suspend fun requestNotificationPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }
    if (permission != null) {
        permissionRequest(arrayOf(permission), onPermissionResult)
    } else {
        val notificationManager = NotificationManagerCompat.from(PermissionHandler.getAppContext())
        if (notificationManager.areNotificationsEnabled()) {
            onPermissionResult(true)
        } else {
            onPermissionResult(false)
        }
    }


}