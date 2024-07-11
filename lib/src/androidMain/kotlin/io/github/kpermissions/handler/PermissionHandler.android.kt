package io.github.kpermissions.handler

import android.content.Context
import io.github.kpermissions.enum.EnumAppPermission
import io.github.kpermissions.handler.permissions.requestAppTrackingPermission
import io.github.kpermissions.handler.permissions.requestBluetoothPermission
import io.github.kpermissions.handler.permissions.requestCameraPermission
import io.github.kpermissions.handler.permissions.requestGalleryPermission
import io.github.kpermissions.handler.permissions.requestLocationAlwaysPermission
import io.github.kpermissions.handler.permissions.requestLocationPermission
import io.github.kpermissions.handler.permissions.requestLocationWhenInUsePermission
import io.github.kpermissions.handler.permissions.requestMicrophonePermission
import io.github.kpermissions.handler.permissions.requestNotificationPermission
import io.github.kpermissions.handler.permissions.requestPhonePermission
import io.github.kpermissions.handler.permissions.requestPhotoPermission
import io.github.kpermissions.handler.permissions.requestReadCalendarPermission
import io.github.kpermissions.handler.permissions.requestReadContactsPermission
import io.github.kpermissions.handler.permissions.requestReadStoragePermission
import io.github.kpermissions.handler.permissions.requestVideoPermission
import io.github.kpermissions.handler.permissions.requestWriteCalendarPermission
import io.github.kpermissions.handler.permissions.requestWriteContactsPermission
import io.github.kpermissions.handler.permissions.requestWriteStoragePermission

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
        internal var onPermissionResult: ((Int, IntArray) -> Unit)? = null

    }

    override fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {
            EnumAppPermission.CALENDAR_READ -> requestReadCalendarPermission(onPermissionResult)
            EnumAppPermission.CALENDAR_WRITE -> requestWriteCalendarPermission(onPermissionResult)

            EnumAppPermission.CONTACTS_WRITE -> requestWriteContactsPermission(onPermissionResult)
            EnumAppPermission.CONTACTS_READ -> requestReadContactsPermission(onPermissionResult)

            EnumAppPermission.LOCATION -> requestLocationPermission(onPermissionResult)
            EnumAppPermission.LOCATION_ALWAYS -> requestLocationAlwaysPermission(onPermissionResult)
            EnumAppPermission.LOCATION_WHEN_IN_USE -> requestLocationWhenInUsePermission(
                onPermissionResult
            )

            EnumAppPermission.WRITE_STORAGE -> requestReadStoragePermission(onPermissionResult)
            EnumAppPermission.READ_STORAGE -> requestWriteStoragePermission(onPermissionResult)

            EnumAppPermission.PHOTO -> requestPhotoPermission(onPermissionResult)
            EnumAppPermission.VIDEO -> requestVideoPermission(onPermissionResult)
            EnumAppPermission.GALLERY -> requestGalleryPermission(onPermissionResult)

            EnumAppPermission.CAMERA -> requestCameraPermission(onPermissionResult)
            EnumAppPermission.MICROPHONE -> requestMicrophonePermission(onPermissionResult)
            EnumAppPermission.NOTIFICATION -> requestNotificationPermission(onPermissionResult)
            EnumAppPermission.PHONE -> requestPhonePermission(onPermissionResult)
            EnumAppPermission.APP_TRACKING_TRANSPARENCY -> requestAppTrackingPermission(
                onPermissionResult
            )

            EnumAppPermission.BLUETOOTH -> requestBluetoothPermission(onPermissionResult)

        }
    }


}