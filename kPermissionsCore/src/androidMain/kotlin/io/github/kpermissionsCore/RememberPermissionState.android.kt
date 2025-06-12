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
actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    val androidPermission = permission.getAndroidName()
    if (androidPermission == null || permission.ignore == PlatformIgnore.Android) {
        onPermissionResult(true)
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

    val androidPermissionState =
        com.google.accompanist.permissions.rememberPermissionState(androidPermission)

    LaunchedEffect(requestedOnce) {
        if (requestedOnce && !hasAskedBefore) {
            prefs.put(permission.name, "true")
            hasAskedBefore = true
        }
    }

    LaunchedEffect(androidPermissionState.status) {
        onPermissionResult(androidPermissionState.status.isGranted)
    }

    val permissionStatus = when (val status = androidPermissionState.status) {
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
            androidPermissionState.launchPermissionRequest()
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit
): List<PermissionState> {
    val androidPermissions = permissions
        .filter { it.ignore != PlatformIgnore.Android }
        .mapNotNull { it.getAndroidName() }

    if (androidPermissions.isEmpty()) {
        onPermissionsResult(true)
        return permissions.map {
            object : PermissionState {
                override val permission: Permission = it
                override var status: PermissionStatus = PermissionStatus.Granted
                override fun launchPermissionRequest() {}
                override fun openAppSettings() {}
            }
        }
    }

    val prefs = SharedPrefs()
    var requestedOnce by remember { mutableStateOf(false) }
    val multiplePermissionState =
        com.google.accompanist.permissions.rememberMultiplePermissionsState(androidPermissions)

    val statuses = permissions.map { perm ->
        val androidName = perm.getAndroidName()
        val permStatus = if (androidName == null || perm.ignore == PlatformIgnore.Android) {
            PermissionStatus.Granted
        } else {
            val result = multiplePermissionState.permissions.find { it.permission == androidName }
            if (result?.status?.isGranted == true) {
                PermissionStatus.Granted
            } else if (result?.status is com.google.accompanist.permissions.PermissionStatus.Denied) {
                val denied =
                    result.status as com.google.accompanist.permissions.PermissionStatus.Denied
                val hasAskedBefore = prefs.get(perm.name) == "true"
                if (!denied.shouldShowRationale && hasAskedBefore) {
                    PermissionStatus.DeniedPermanently
                } else {
                    PermissionStatus.Denied
                }
            } else {
                PermissionStatus.Denied
            }
        }

        object : PermissionState {
            override val permission: Permission = perm
            override var status: PermissionStatus = permStatus

            override fun launchPermissionRequest() {
                requestedOnce = true
                multiplePermissionState.launchMultiplePermissionRequest()
            }

            override fun openAppSettings() {
                openAppSettingsPlatform()
            }
        }
    }

    LaunchedEffect(requestedOnce) {
        if (requestedOnce) {
            permissions.forEach { perm ->
                prefs.put(perm.name, "true")
            }
        }
    }

    LaunchedEffect(multiplePermissionState.permissions) {
        val allGranted = multiplePermissionState.permissions.all { it.status.isGranted }
        onPermissionsResult(allGranted)
    }

    return statuses
}
