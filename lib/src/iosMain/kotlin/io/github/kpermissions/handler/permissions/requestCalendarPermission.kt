package io.github.kpermissions.handler.permissions

import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusDenied
import platform.EventKit.EKAuthorizationStatusNotDetermined
import platform.EventKit.EKEntityType
import platform.EventKit.EKEventStore
import platform.Foundation.NSError


fun requestCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val eventStore = EKEventStore
    eventStore.initialize()

    when (eventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent)) {
        EKAuthorizationStatusAuthorized -> {
            onPermissionResult(true)
        }

        EKAuthorizationStatusDenied -> {
            onPermissionResult(false)
        }

        EKAuthorizationStatusNotDetermined -> {
            EKEventStore().requestAccessToEntityType(
                EKEntityType.EKEntityTypeEvent
            ) objCBlock@{ granted: Boolean, e: NSError? ->
                if (e != null) {
                    print("error is $e ")

                }
                onPermissionResult(granted)
            }
        }

        else -> {
            onPermissionResult(false)
        }
    }
}