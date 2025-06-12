package io.github.kpermissionsCamera

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PermissionStatusRegistry
import io.github.kpermissionsCore.PlatformIgnore
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

actual object CameraPermission : Permission {
    init {
        PermissionStatusRegistry.register("camera", ::getCameraPermissionStatus)
    }

    override val name: String
        get() = "camera"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                callback(granted)
            }
        }

    override val type: io.github.kpermissionsCore.PermissionType
        get() = io.github.kpermissionsCore.PermissionType.Camera

    override var ignore: PlatformIgnore = PlatformIgnore.None
}