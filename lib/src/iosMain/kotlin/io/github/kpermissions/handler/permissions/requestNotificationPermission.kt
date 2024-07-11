package io.github.kpermissions.handler.permissions

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

fun requestNotificationPermission(onPermissionResult: (Boolean) -> Unit) {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    center.requestAuthorizationWithOptions(
        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        completionHandler = { granted, error ->
            if (error != null) {
                // Handle error
                onPermissionResult(false)
            } else if (granted) {
                // Permission granted
                onPermissionResult(true)
            } else {
                // Permission denied
                onPermissionResult(false)
            }
        }
    )
}