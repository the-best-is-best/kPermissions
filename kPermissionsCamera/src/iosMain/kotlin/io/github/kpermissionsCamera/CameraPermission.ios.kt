package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.IOSRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PermissionStatusRegistry
import io.github.kpermissionsCore.PlatformIgnore
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import kotlin.experimental.ExperimentalObjCName


@Composable
internal actual fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return IOSRememberPermissionStateCore(
        permission,
        permissionRequest = { callback ->
            PHPhotoLibrary.requestAuthorization { status ->
                when (status) {
                    PHAuthorizationStatusAuthorized,
                    PHAuthorizationStatusLimited -> callback(true)

                    else -> callback(false)
                }
            }
        },
        onResult = onResult,
    )

}

private fun getGalleryPermissionStatus(): PermissionStatus {
    return when (PHPhotoLibrary.authorizationStatus()) {
        PHAuthorizationStatusAuthorized -> PermissionStatus.Granted
        PHAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
        PHAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
        PHAuthorizationStatusNotDetermined -> PermissionStatus.Denied
        PHAuthorizationStatusLimited -> PermissionStatus.Granted // iOS 14+
        else -> PermissionStatus.Denied
    }
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("registerCameraPermission")
actual fun CameraPermission.register(ignore: PlatformIgnore) {
    if (isCameraPermissionRegistered) return
    isCameraPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        CameraPermission::class
    ) { permission, onResult ->
        PermissionStatusRegistry.register(permission.name) {
            getGalleryPermissionStatus()
        }
        CameraPermissionState(permission as CameraPermission, onResult)

    }

}