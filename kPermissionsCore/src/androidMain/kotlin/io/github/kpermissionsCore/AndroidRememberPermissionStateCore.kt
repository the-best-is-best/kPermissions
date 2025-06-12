package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import io.github.compose_utils_core.SharedPrefs

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AndroidRememberPermissionStateCore(
    androidPermission: String?,
    permission: Permission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    if (androidPermission == null || permission.ignore == PlatformIgnore.Android) {
        onResult(true)
        return object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }
    val prefs = SharedPrefs()
    var requestedOnce by remember { mutableStateOf(false) }
    var hasAskedBefore by remember { mutableStateOf(prefs.get(permission.name) == "true") }

    val permissionState =
        com.google.accompanist.permissions.rememberPermissionState(androidPermission)

    LaunchedEffect(permissionState.status, requestedOnce) {
        if (requestedOnce && !hasAskedBefore) {
            prefs.put(permission.name, "true")
            hasAskedBefore = true
        }
        onResult(permissionState.status.isGranted)
    }

    val permissionStatus = when (val status = permissionState.status) {
        is com.google.accompanist.permissions.PermissionStatus.Granted -> PermissionStatus.Granted
        is com.google.accompanist.permissions.PermissionStatus.Denied -> {
            if (!status.shouldShowRationale && hasAskedBefore) {
                PermissionStatus.DeniedPermanently
            } else {
                PermissionStatus.Denied
            }
        }
    }

    return object : PermissionState {
        override val permission: Permission = permission
        override var status: PermissionStatus = permissionStatus

        override fun launchPermissionRequest() {
            requestedOnce = true
            permissionState.launchPermissionRequest()
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }
}
