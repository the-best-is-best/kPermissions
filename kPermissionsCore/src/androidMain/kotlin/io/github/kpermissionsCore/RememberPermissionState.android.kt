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
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.getIgnore

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    val androidPermission = permission.androidPermissionName

    val currentSdk = android.os.Build.VERSION.SDK_INT
    val minSdk = permission.minSdk
    val maxSdk = permission.maxSdk

    if (androidPermission == null ||
        permission.getIgnore() == PlatformIgnore.Android ||
        (minSdk != null && currentSdk < minSdk) ||
        (maxSdk != null && currentSdk > maxSdk)
    ) {
        onPermissionResult(true)
        return object : PermissionState {
            override val permission: Permission = permission
            override val status: PermissionStatus = PermissionStatus.Granted
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
        override val status: PermissionStatus = permissionStatus

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
    val currentSdk = android.os.Build.VERSION.SDK_INT

    val filteredPermissions = remember(permissions) {
        permissions.filter {
            it.getIgnore() != PlatformIgnore.Android &&
                    it.androidPermissionName != null &&
                    (it.minSdk?.let { sdk -> currentSdk >= sdk } ?: true) &&
                    (it.maxSdk?.let { sdk -> currentSdk <= sdk } ?: true)
        }
    }

    val androidPermissions = filteredPermissions.mapNotNull { it.androidPermissionName }

    if (filteredPermissions.isEmpty()) {
        LaunchedEffect(Unit) { onPermissionsResult(true) }
        return permissions.map {
            object : PermissionState {
                override val permission = it
                override val status = PermissionStatus.Granted
                override fun launchPermissionRequest() {}
                override fun openAppSettings() {}
            }
        }
    }

    val prefs = remember { SharedPrefs() }
    var requestedOnce by remember { mutableStateOf(false) }
    val multiplePermissionState =
        com.google.accompanist.permissions.rememberMultiplePermissionsState(androidPermissions)

    val states = filteredPermissions.map { perm ->
        val androidName = perm.androidPermissionName
        var statusState by remember { mutableStateOf<PermissionStatus>(PermissionStatus.Denied) }

        val result = multiplePermissionState.permissions.find { it.permission == androidName }
        println("Permission ${perm.name} raw result = ${result?.status}")

        statusState = when {
            result?.status?.isGranted == true -> PermissionStatus.Granted
            result?.status is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                val denied =
                    result.status as com.google.accompanist.permissions.PermissionStatus.Denied
                val asked = prefs.get(perm.name) == "true"
                if (!denied.shouldShowRationale && asked) PermissionStatus.DeniedPermanently else PermissionStatus.Denied
            }

            else -> PermissionStatus.Denied
        }

        object : PermissionState {
            override val permission = perm
            override val status: PermissionStatus
                get() = statusState
            override fun launchPermissionRequest() {
                requestedOnce = true
                multiplePermissionState.launchMultiplePermissionRequest()
            }
            override fun openAppSettings() {
                openAppSettingsPlatform()
            }
        }
    }

    val ignoredStates = (permissions - filteredPermissions.toSet()).map {
        object : PermissionState {
            override val permission = it
            override val status = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }

    LaunchedEffect(requestedOnce) {
        if (requestedOnce) {
            filteredPermissions.forEach { prefs.put(it.name, "true") }
        }
    }

    LaunchedEffect(multiplePermissionState.permissions) {
        val allGranted = multiplePermissionState.permissions.all { it.status.isGranted }
        onPermissionsResult(allGranted)
    }

    return states + ignoredStates
}