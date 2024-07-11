package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission
import io.github.kpermissions.handler.permissions.requestAppTrackingPermission
import io.github.kpermissions.handler.permissions.requestBlueToothPermission
import io.github.kpermissions.handler.permissions.requestCalendarPermission
import io.github.kpermissions.handler.permissions.requestCameraPermission
import io.github.kpermissions.handler.permissions.requestContactsPermission
import io.github.kpermissions.handler.permissions.requestGalleryPermission
import io.github.kpermissions.handler.permissions.requestLocationAlwaysPermission
import io.github.kpermissions.handler.permissions.requestLocationPermission
import io.github.kpermissions.handler.permissions.requestLocationWhenInUsePermission
import io.github.kpermissions.handler.permissions.requestMicrophonePermission
import io.github.kpermissions.handler.permissions.requestNotificationPermission
import io.github.kpermissions.handler.permissions.requestPhonePermission
import io.github.kpermissions.handler.permissions.requestPhotoPermission
import io.github.kpermissions.handler.permissions.requestStoragePermission
import io.github.kpermissions.handler.permissions.requestVideoPermission

actual class PermissionHandler actual constructor() : PermissionCallback {


    override fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {
            EnumAppPermission.CALENDAR_WRITE,
            EnumAppPermission.CALENDAR_READ -> requestCalendarPermission(onPermissionResult)

            EnumAppPermission.CONTACTS_WRITE,
            EnumAppPermission.CONTACTS_READ -> requestContactsPermission(onPermissionResult)

            EnumAppPermission.LOCATION -> requestLocationPermission(onPermissionResult)
            EnumAppPermission.LOCATION_ALWAYS -> requestLocationAlwaysPermission(onPermissionResult)
            EnumAppPermission.LOCATION_WHEN_IN_USE -> requestLocationWhenInUsePermission(
                onPermissionResult
            )

            EnumAppPermission.WRITE_STORAGE -> requestStoragePermission(onPermissionResult)
            EnumAppPermission.READ_STORAGE -> requestStoragePermission(onPermissionResult)

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

            EnumAppPermission.BLUETOOTH -> requestBlueToothPermission(onPermissionResult)

        }
    }


    actual companion object {
        //        fun init (openSetting:Boolean){
//            this.openSetting = openSetting
//        }
        internal actual var openSetting: Boolean = false
    }
}