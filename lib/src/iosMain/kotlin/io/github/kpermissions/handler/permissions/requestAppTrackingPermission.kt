package io.github.kpermissions.handler.permissions

import platform.AppTrackingTransparency.ATTrackingManager
import platform.AppTrackingTransparency.ATTrackingManagerAuthorizationStatusAuthorized
import platform.AppTrackingTransparency.ATTrackingManagerAuthorizationStatusNotDetermined

fun requestAppTrackingPermission(onPermissionResult: (Boolean) -> Unit) {
    val currentStatus = ATTrackingManager.trackingAuthorizationStatus()

    when (currentStatus) {
        ATTrackingManagerAuthorizationStatusAuthorized -> {
            // Permission already granted
            onPermissionResult(true)
        }

        ATTrackingManagerAuthorizationStatusNotDetermined -> {
            // Request permission
            ATTrackingManager.requestTrackingAuthorizationWithCompletionHandler { status ->
                when (status) {
                    ATTrackingManagerAuthorizationStatusAuthorized -> {
                        // Permission granted
                        onPermissionResult(true)
                    }

                    else -> {
                        // Permission denied or restricted
                        onPermissionResult(false)
                    }
                }
            }
        }

        else -> {
            // Permission denied or restricted
            onPermissionResult(false)
        }
    }
}
