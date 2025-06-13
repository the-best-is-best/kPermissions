package io.github.kpermissionsCamera

import io.github.kPermissions_api.Permission
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PermissionStatusRegistry
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType


private fun getCameraPermissionStatus(): PermissionStatus {
    return when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.Granted
        AVAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
        AVAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
        AVAuthorizationStatusNotDetermined -> PermissionStatus.Denied
        else -> PermissionStatus.Denied
    }
}

internal fun cameraPermissionRequest(): ((Boolean) -> Unit) -> Unit = { callback ->
    val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)

    if (status == AVAuthorizationStatusAuthorized) {
        callback(true)
    } else {
        AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
            callback(granted)
        }
    }
}

internal fun registerIosProvider() {
    PermissionStatusRegistry.register(
        "camera",
        ::getCameraPermissionStatus
    )
}

actual object CameraPermission : Permission {
    init {
        registerIosProvider()
    }

    override val name: String
        get() = "camera"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = cameraPermissionRequest()

}