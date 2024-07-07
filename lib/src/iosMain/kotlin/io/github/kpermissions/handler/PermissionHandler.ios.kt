package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission
import io.github.kpermissions.handler.permissions.requestCalendarPermission
import io.github.kpermissions.handler.permissions.requestContactsPermission

actual class PermissionHandler actual constructor() : PermissionCallback {


    override fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {
            EnumAppPermission.CALENDARWRITE,
            EnumAppPermission.CALENDARREAD -> requestCalendarPermission(onPermissionResult)

            EnumAppPermission.CONTACTSWRITE,
            EnumAppPermission.CONTACTSREAD -> requestContactsPermission(onPermissionResult)
            EnumAppPermission.LOCATION -> TODO()
            EnumAppPermission.LOCATION_ALWAYS -> TODO()
            EnumAppPermission.LOCATION_WHEN_IN_USE -> TODO()
            EnumAppPermission.STORAGE -> TODO()
            EnumAppPermission.CAMERA -> TODO()
            EnumAppPermission.MICROPHONE -> TODO()
            EnumAppPermission.NOTIFICATION -> TODO()
            EnumAppPermission.PHONE -> TODO()
            EnumAppPermission.SENSORS -> TODO()
            EnumAppPermission.SMS -> TODO()
            EnumAppPermission.APP_TRACKING_TRANSPARENCY -> TODO()
            EnumAppPermission.BLUETOOTH -> TODO()
            EnumAppPermission.MOTION -> TODO()
            EnumAppPermission.PHOTOS -> TODO()
            EnumAppPermission.REMINDERS -> TODO()
            EnumAppPermission.SPEECH -> TODO()
        }
    }


    actual companion object {
        //        fun init (openSetting:Boolean){
//            this.openSetting = openSetting
//        }
        internal actual var openSetting: Boolean = false
    }
}