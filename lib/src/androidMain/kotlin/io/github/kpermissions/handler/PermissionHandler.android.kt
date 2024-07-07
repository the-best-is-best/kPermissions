package io.github.kpermissions.handler

import android.content.Context
import io.github.kpermissions.enum.EnumAppPermission
import io.github.kpermissions.handler.permissions.requestReadCalendarPermission
import io.github.kpermissions.handler.permissions.requestReadContactsPermission
import io.github.kpermissions.handler.permissions.requestWriteCalendarPermission

actual class PermissionHandler actual constructor() : PermissionCallback {

    actual companion object {

        private lateinit var appContext: Context

        fun init(context: Context, openSetting: Boolean = false) {
            appContext = context
            this.openSetting = openSetting
        }


        fun getAppContext(): Context {

            if (!::appContext.isInitialized) {
                throw IllegalStateException("PermissionHandler has not been initialized with context.")
            }
            return appContext
        }

        internal actual var openSetting: Boolean = false
    }

    override fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {
            EnumAppPermission.CALENDARREAD -> requestReadCalendarPermission(onPermissionResult)
            EnumAppPermission.CALENDARWRITE -> requestWriteCalendarPermission(onPermissionResult)

            EnumAppPermission.CONTACTSWRITE -> requestWriteCalendarPermission(onPermissionResult)
            EnumAppPermission.CONTACTSREAD -> requestReadContactsPermission(onPermissionResult)

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
}