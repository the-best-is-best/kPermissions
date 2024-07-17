package io.github.kpermissions.handler.permissions

import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

fun requestCameraPermission(onPermissionResult: (Boolean) -> Unit) {

    val authorizationStatus = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
    when (authorizationStatus) {
        AVAuthorizationStatusAuthorized -> {
            onPermissionResult(true)
        }

        AVAuthorizationStatusNotDetermined -> {
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                onPermissionResult(granted)
            }
        }

        else -> {
            onPermissionResult(false)
        }
    }
}
