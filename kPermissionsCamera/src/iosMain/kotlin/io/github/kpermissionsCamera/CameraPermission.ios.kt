package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.IOSRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
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
import kotlin.experimental.ExperimentalObjCName


@Composable
internal actual fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return IOSRememberPermissionStateCore(
        permission,
        permissionRequest = { callback ->
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                callback(granted)
            }
        },
        onResult = onResult,
    )

}

private fun getCameraPermissionStatus(): PermissionStatus {
    return when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.Granted
        AVAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
        AVAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
        AVAuthorizationStatusNotDetermined -> PermissionStatus.Denied
        else -> PermissionStatus.Denied
    }
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("registerCameraPermission")
actual fun CameraPermission.register() {
    if (isCameraPermissionRegistered) return
    isCameraPermissionRegistered = true

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        CameraPermission::class
    ) { permission, onResult ->
        PermissionStatusRegistry.register(permission.name) {
            getCameraPermissionStatus()
        }
        CameraPermissionState(permission as CameraPermission, onResult)

    }

}