package io.github.kpermissions.handler

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual class PermissionHandler actual constructor() {

    companion object {

        private lateinit var appContext: Context

        fun init(context: Context) {
            appContext = context
        }


        fun getAppContext(): Context {

            if (!::appContext.isInitialized) {
                throw IllegalStateException("PermissionHandler has not been initialized with context.")
            }
            return appContext
        }


    }


    actual fun openAppSettings() {
        val context = getAppContext()
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle case where the settings activity is not found
            e.printStackTrace()
            println("open setting error ${e.localizedMessage}")
        }
    }

    actual fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        when (permission) {

            EnumAppPermission.CALENDAR_READ ->
                CoroutineScope(Dispatchers.Main).launch {
                    requestReadCalendarPermission(onPermissionResult)
                }

            EnumAppPermission.CALENDAR_WRITE ->
                CoroutineScope(Dispatchers.Main).launch {
                    requestWriteCalendarPermission(onPermissionResult)
                }

            EnumAppPermission.CONTACTS_WRITE -> CoroutineScope(Dispatchers.Main).launch {
                requestWriteContactsPermission(onPermissionResult)
            }

            EnumAppPermission.CONTACTS_READ -> CoroutineScope(Dispatchers.Main).launch {
                requestReadContactsPermission(onPermissionResult)
            }

            EnumAppPermission.LOCATION -> CoroutineScope(Dispatchers.Main).launch {
                requestLocationPermission(onPermissionResult)
            }

            EnumAppPermission.LOCATION_ALWAYS -> CoroutineScope(Dispatchers.Main).launch {
                requestLocationAlwaysPermission(onPermissionResult)
            }

            EnumAppPermission.LOCATION_WHEN_IN_USE ->
                CoroutineScope(Dispatchers.Main).launch {
                    requestLocationWhenInUsePermission(
                        onPermissionResult
                    )
                }

            EnumAppPermission.WRITE_STORAGE -> CoroutineScope(Dispatchers.Main).launch {
                requestReadStoragePermission(onPermissionResult)
            }

            EnumAppPermission.READ_STORAGE -> CoroutineScope(Dispatchers.Main).launch {

                requestWriteStoragePermission(onPermissionResult)
            }

            EnumAppPermission.PHOTO -> CoroutineScope(Dispatchers.Main).launch {
                requestPhotoPermission(onPermissionResult)
            }

            EnumAppPermission.VIDEO -> CoroutineScope(Dispatchers.Main).launch {
                requestVideoPermission(onPermissionResult)
            }

            EnumAppPermission.GALLERY -> CoroutineScope(Dispatchers.Main).launch {
                requestGalleryPermission(onPermissionResult)
            }

            EnumAppPermission.CAMERA -> CoroutineScope(Dispatchers.Main).launch {
                requestCameraPermission(onPermissionResult)
            }

            EnumAppPermission.MICROPHONE -> CoroutineScope(Dispatchers.Main).launch {
                requestMicrophonePermission(onPermissionResult)
            }

            EnumAppPermission.NOTIFICATION -> CoroutineScope(Dispatchers.Main).launch {
                requestNotificationPermission(onPermissionResult)
            }

            EnumAppPermission.PHONE -> CoroutineScope(Dispatchers.Main).launch {
                requestPhonePermission(onPermissionResult)
            }

            EnumAppPermission.APP_TRACKING_TRANSPARENCY -> CoroutineScope(Dispatchers.Main).launch {
                requestAppTrackingPermission(
                    onPermissionResult
                )
            }

            EnumAppPermission.BLUETOOTH -> CoroutineScope(Dispatchers.Main).launch {
                requestBluetoothPermission(onPermissionResult)
            }
        }
    }
}