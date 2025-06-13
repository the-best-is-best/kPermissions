package io.github.kpermissionsCamera

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
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

actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = cameraPermissionRequest()

    override fun getPermissionStatus(): PermissionStatus {
        return getCameraPermissionStatus()
    }

    override var minSdk: Int? = null
    override var maxSdk: Int? = null


}