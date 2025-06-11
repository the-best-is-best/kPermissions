package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.openAppSettingsPlatform
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType


@Composable
internal actual fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    var status by remember { mutableStateOf(getCameraPermissionStatus()) }

    LaunchedEffect(Unit) {
        onResult(getCameraPermissionStatus() == PermissionStatus.Granted)
    }

    LaunchedEffect(Unit) {
        onResult(status == PermissionStatus.Granted)
    }

    OnAppResumed {
        val newStatus = getCameraPermissionStatus()
        if (newStatus != status) {
            status = newStatus
            onResult(status == PermissionStatus.Granted)
        }
    }



    return object : PermissionState {
        override val permission: Permission = permission


        override var status: PermissionStatus
            get() = status
            set(_) {}

        override fun launchPermissionRequest() {
            AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                onResult(granted)
            }
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }
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


@Composable
fun OnAppResumed(onResume: () -> Unit) {
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(Unit) {
        val observer = platform.Foundation.NSNotificationCenter.defaultCenter.addObserverForName(
            name = platform.UIKit.UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null
        ) { _ ->
            currentOnResume()
        }

        onDispose {
            platform.Foundation.NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}
