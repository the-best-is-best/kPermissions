package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.getIgnore


@Composable
actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    val skip =
        permission.getIgnore() == PlatformIgnore.IOS ||
                (permission.minSdk?.let { currentIosVersion < it } ?: false) ||
                (permission.maxSdk?.let { currentIosVersion > it } ?: false)

    if (skip) {
        onPermissionResult(true)
        return object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }

    fun getStatus() = permission.getPermissionStatus()

    var stateValue by remember { mutableStateOf(getStatus()) }

    LaunchedEffect(Unit) {
        onPermissionResult(stateValue == PermissionStatus.Granted)
    }

    OnAppResumed {
        val newStatus = getStatus()
        if (newStatus != stateValue) {
            stateValue = newStatus
            onPermissionResult(newStatus == PermissionStatus.Granted)
        }
    }

    return object : PermissionState {
        override val permission: Permission = permission
        override var status: PermissionStatus
            get() = stateValue
            set(value) {
                stateValue = value
            }

        override fun launchPermissionRequest() {
            permission.permissionRequest { granted ->
                val result = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                status = result
                onPermissionResult(granted)
            }
        }

        override fun openAppSettings() = openAppSettingsPlatform()
    }
}

@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit
): List<PermissionState> {
    val filtered = permissions.filter {
        it.getIgnore() != PlatformIgnore.IOS &&
                (it.minSdk?.let { min -> currentIosVersion >= min } ?: true) &&
                (it.maxSdk?.let { max -> currentIosVersion <= max } ?: true)
    }

    val ignoredPermissions = permissions - filtered.toSet()
    val ignoredStates = ignoredPermissions.map {
        object : PermissionState {
            override val permission: Permission = it
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }

    fun getStatuses(): Map<String, PermissionStatus> =
        filtered.associate { it.name to it.getPermissionStatus() }

    var stateMap by remember { mutableStateOf(getStatuses()) }

    LaunchedEffect(Unit) {
        val allGranted = permissions.all {
            stateMap[it.name] == PermissionStatus.Granted || it in ignoredPermissions
        }
        onPermissionsResult(allGranted)
    }

    OnAppResumed {
        val newStatuses = getStatuses()
        if (newStatuses != stateMap) {
            stateMap = newStatuses
            val allGranted = permissions.all {
                newStatuses[it.name] == PermissionStatus.Granted || it in ignoredPermissions
            }
            onPermissionsResult(allGranted)
        }
    }

    val actualStates = filtered.map { permission ->
        object : PermissionState {
            override val permission: Permission = permission

            override var status: PermissionStatus
                get() = stateMap[permission.name] ?: PermissionStatus.Granted
                set(value) {
                    stateMap = stateMap.toMutableMap().apply {
                        this[permission.name] = value
                    }
                }

            override fun launchPermissionRequest() {
                permission.permissionRequest { granted ->
                    status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                    val allGranted = permissions.all {
                        (stateMap[it.name] == PermissionStatus.Granted) || it in ignoredPermissions
                    }
                    onPermissionsResult(allGranted)
                }
            }

            override fun openAppSettings() = openAppSettingsPlatform()
        }
    }

    return actualStates + ignoredStates
}