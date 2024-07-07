package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission

actual class PermissionHandler actual constructor() : PermissionCallback {
    override fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {
            EnumAppPermission.CALENDAR -> onPermissionResult(true)
            EnumAppPermission.CONTACTS -> onPermissionResult(true)
            EnumAppPermission.LOCATION -> onPermissionResult(true)
            EnumAppPermission.LOCATION_ALWAYS -> onPermissionResult(true)
            EnumAppPermission.LOCATION_WHEN_IN_USE -> onPermissionResult(true)
            EnumAppPermission.STORAGE -> onPermissionResult(true)
            EnumAppPermission.CAMERA -> onPermissionResult(true)
            EnumAppPermission.MICROPHONE -> onPermissionResult(true)
            EnumAppPermission.NOTIFICATION -> onPermissionResult(true)
            EnumAppPermission.PHONE -> onPermissionResult(true)
            EnumAppPermission.SENSORS -> onPermissionResult(true)
            EnumAppPermission.SMS -> onPermissionResult(true)
            EnumAppPermission.APP_TRACKING_TRANSPARENCY -> onPermissionResult(true)
            EnumAppPermission.BLUETOOTH -> onPermissionResult(true)
            EnumAppPermission.MOTION -> onPermissionResult(true)
            EnumAppPermission.PHOTOS -> onPermissionResult(true)
            EnumAppPermission.REMINDERS -> onPermissionResult(true)
            EnumAppPermission.SPEECH -> onPermissionResult(true)
        }
    }
}