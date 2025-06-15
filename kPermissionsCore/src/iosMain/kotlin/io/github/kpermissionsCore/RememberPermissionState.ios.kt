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
    val isIgnored = permission.getIgnore() == PlatformIgnore.IOS
    val isOutOfSdk = (permission.minSdk?.let { currentIosVersion < it } ?: false) ||
            (permission.maxSdk?.let { currentIosVersion > it } ?: false)
    val unavailable = !permission.isServiceAvailable()

    val fixedStatus = when {
        isIgnored || isOutOfSdk -> PermissionStatus.Granted
        unavailable -> PermissionStatus.Unavailable
        else -> null
    }

    if (fixedStatus != null) {
        LaunchedEffect(permission) {
            onPermissionResult(fixedStatus == PermissionStatus.Granted)
        }
        return object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus
                get() = fixedStatus
                set(_) {}
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
            override fun checkPermissionStatus(): PermissionStatus = fixedStatus
        }
    }

    fun getStatus() = permission.getPermissionStatus()
    var stateValue by remember { mutableStateOf(getStatus()) }

    // فقط نطلق onPermissionResult عندما تتغير الحالة
    LaunchedEffect(stateValue) {
        onPermissionResult(stateValue == PermissionStatus.Granted)
    }

    OnAppResumed {
        val newStatus = getStatus()
        if (newStatus != stateValue) {
            stateValue = newStatus
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
                val newStatus =
                    if (granted) PermissionStatus.Granted else permission.checkPermissionStatus()
                if (newStatus != stateValue) {
                    stateValue = newStatus
                }
            }
        }

        override fun openAppSettings() = openAppSettingsPlatform()
        override fun checkPermissionStatus(): PermissionStatus {
            val refreshed = permission.checkPermissionStatus()
            if (refreshed != stateValue) {
                stateValue = refreshed
            }
            return refreshed
        }
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
                (it.maxSdk?.let { max -> currentIosVersion <= max } ?: true) &&
                it.isServiceAvailable()
    }
    val ignoredPermissions = permissions - filtered.toSet()

    val ignoredStates = ignoredPermissions.map { perm ->
        val isIgnored = perm.getIgnore() == PlatformIgnore.IOS
        val isOutOfSdk = (perm.minSdk?.let { currentIosVersion < it } ?: false) ||
                (perm.maxSdk?.let { currentIosVersion > it } ?: false)
        val unavailable = !perm.isServiceAvailable()

        val fixedStatus = when {
            isIgnored || isOutOfSdk -> PermissionStatus.Granted
            unavailable -> PermissionStatus.Unavailable
            else -> perm.checkPermissionStatus()
        }

        object : PermissionState {
            override val permission: Permission = perm
            override var status: PermissionStatus
                get() = fixedStatus
                set(_) {}
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
            override fun checkPermissionStatus(): PermissionStatus = fixedStatus
        }
    }

    fun getStatuses(): Map<String, PermissionStatus> =
        filtered.associate { it.name to it.getPermissionStatus() }

    var stateMap by remember { mutableStateOf(getStatuses()) }

    fun checkAllGranted(): Boolean = permissions.all { perm ->
        val isIgnored = perm.getIgnore() == PlatformIgnore.IOS
        val isOutOfSdk = (perm.minSdk?.let { currentIosVersion < it } ?: false) ||
                (perm.maxSdk?.let { currentIosVersion > it } ?: false)
        val unavailable = !perm.isServiceAvailable()

        when {
            isIgnored || isOutOfSdk -> true
            unavailable -> false
            else -> stateMap[perm.name] == PermissionStatus.Granted
        }
    }

    LaunchedEffect(permissions) {
        onPermissionsResult(checkAllGranted())
    }

    OnAppResumed {
        val newStatuses = getStatuses()
        if (newStatuses != stateMap) {
            stateMap = newStatuses
            onPermissionsResult(checkAllGranted())
        }
    }

    val actualStates = filtered.map { permission ->
        object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus
                get() = stateMap[permission.name] ?: PermissionStatus.Unavailable
                set(value) {
                    stateMap = stateMap.toMutableMap().apply {
                        this[permission.name] = value
                    }
                }

            override fun launchPermissionRequest() {
                if (permissions.isEmpty()) {
                    onPermissionsResult(true)
                    return
                }
                requestPermissionsSequentially(filtered) { allGranted ->
                    onPermissionsResult(allGranted)
                }
            }


            override fun openAppSettings() = openAppSettingsPlatform()
            override fun checkPermissionStatus(): PermissionStatus {
                val refreshed = permission.checkPermissionStatus()
                status = refreshed
                return refreshed
            }
        }
    }

    return actualStates + ignoredStates
}

