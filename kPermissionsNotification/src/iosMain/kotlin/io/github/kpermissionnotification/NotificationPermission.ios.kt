package io.github.kpermissionnotification

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.DISPATCH_TIME_FOREVER
import platform.darwin.dispatch_semaphore_create
import platform.darwin.dispatch_semaphore_signal
import platform.darwin.dispatch_semaphore_wait

actual object NotificationPermission : Permission {

    override val name: String = "notification"

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null
    override val minSdk: Int? get() = _minSdk
    override val maxSdk: Int? get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override fun isServiceAvailable(): Boolean {
        return true
    }

    override fun getPermissionStatus(): PermissionStatus {
        var result: PermissionStatus = PermissionStatus.Denied

        val semaphore = dispatch_semaphore_create(0)
        UNUserNotificationCenter.currentNotificationCenter()
            .getNotificationSettingsWithCompletionHandler { settings ->
                result = when (settings?.authorizationStatus) {
                    UNAuthorizationStatusAuthorized, UNAuthorizationStatusProvisional -> {
                        PermissionStatus.Granted
                    }

                    UNAuthorizationStatusDenied -> {
                        PermissionStatus.DeniedPermanently
                    }

                    UNAuthorizationStatusNotDetermined -> {
                        PermissionStatus.Denied
                    }

                    else -> {
                        PermissionStatus.Denied
                    }
                }
                dispatch_semaphore_signal(semaphore)
            }

        dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER)
        return result
    }


    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            val center = UNUserNotificationCenter.currentNotificationCenter()
            center.requestAuthorizationWithOptions(
                UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
            ) { granted, _ ->
                callback(granted)
            }
        }
}
