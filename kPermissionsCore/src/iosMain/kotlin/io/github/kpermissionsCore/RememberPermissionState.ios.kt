package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.getIgnore
import kotlinx.coroutines.launch

internal val PermissionStatus.canRequest: Boolean
    get() = this == PermissionStatus.Denied || this == PermissionStatus.NotDeclared


@Composable
actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    val isIgnored = permission.getIgnore() == PlatformIgnore.IOS
    val isOutOfSdk = (permission.minSdk?.let { currentIosVersion < it } ?: false) ||
            (permission.maxSdk?.let { currentIosVersion > it } ?: false)

    val fixedStatus = when {
        isIgnored || isOutOfSdk -> PermissionStatus.Granted
        else -> null
    }
    val coroutineScope = rememberCoroutineScope()

    if (fixedStatus != null) {
        LaunchedEffect(permission) {
            onPermissionResult(fixedStatus == PermissionStatus.Granted)
        }
        return object : PermissionState {
            override val permission = permission
            override var status: PermissionStatus = fixedStatus
            override fun launchPermissionRequest() {}
            override fun openAppSettings() = Unit
            override suspend fun refreshStatus() {
                val newStatus = permission.getPermissionStatus()
                if (newStatus != status) {
                    status = newStatus
                    onPermissionResult(newStatus == PermissionStatus.Granted)
                }
            }
        }
    }

    var stateValue by remember { mutableStateOf<PermissionStatus>(PermissionStatus.Denied) }


    return object : PermissionState {
        override val permission = permission

        override var status: PermissionStatus
            get() = stateValue
            set(value) {
                stateValue = value
            }

        override fun launchPermissionRequest() {
            if (stateValue.canRequest) {
                permission.permissionRequest { granted ->
                    if (granted) {
                        coroutineScope.launch {
                            val serviceAvailable = permission.isServiceAvailable()
                            stateValue =
                                if (serviceAvailable) PermissionStatus.Granted else PermissionStatus.Unavailable
                            onPermissionResult(serviceAvailable)
                        }
                    } else {
                        stateValue = PermissionStatus.Denied
                        onPermissionResult(false)
                    }
                }
            }
        }



        override fun openAppSettings() = openAppSettingsPlatform()

        override suspend fun refreshStatus() {
            val newStatus = permission.getPermissionStatus()
            if (newStatus != status) {
                status = newStatus
                onPermissionResult(newStatus == PermissionStatus.Granted)
            }
        }
    }
}

@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit
): List<PermissionState> {
    val coroutineScope = rememberCoroutineScope()

    // State map لتخزين صلاحية كل Permission
    var serviceAvailableMap by remember { mutableStateOf<Map<Permission, Boolean>>(emptyMap()) }

    // نفذ فحص isServiceAvailable لكل صلاحية بشكل غير متزامن مرة واحدة عند البداية أو عند تغيير permissions
    LaunchedEffect(permissions) {
        val results = mutableMapOf<Permission, Boolean>()
        for (perm in permissions) {
            results[perm] = perm.isServiceAvailable() // هنا تستدعي suspend function
        }
        serviceAvailableMap = results
    }

    // الآن يمكننا فلترة الصلاحيات بناءً على نتائج isServiceAvailable المحفوظة في serviceAvailableMap
    val filtered = permissions.filter { perm ->
        perm.getIgnore() != PlatformIgnore.IOS &&
                (perm.minSdk?.let { currentIosVersion >= it } ?: true) &&
                (perm.maxSdk?.let { currentIosVersion <= it } ?: true) &&
                (serviceAvailableMap[perm] ?: false) // نتأكد من أنها متاحة
    }

    val ignoredPermissions = permissions - filtered.toSet()

    val ignoredStates = ignoredPermissions.map { perm ->
        val isIgnored = perm.getIgnore() == PlatformIgnore.IOS
        val isOutOfSdk = (perm.minSdk?.let { currentIosVersion < it } ?: false) ||
                (perm.maxSdk?.let { currentIosVersion > it } ?: false)
        val unavailable = !(serviceAvailableMap[perm] ?: false)

        val fixedStatus = when {
            isIgnored || isOutOfSdk -> PermissionStatus.Granted
            unavailable -> PermissionStatus.Unavailable
            else -> perm.getPermissionStatus()
        }

        object : PermissionState {
            override val permission: Permission = perm
            override var status: PermissionStatus
                get() = fixedStatus
                set(_) {}
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
            override suspend fun refreshStatus() {}
        }
    }

    fun getStatuses(): Map<String, PermissionStatus> =
        filtered.associate { it.name to it.getPermissionStatus() }

    var stateMap by remember { mutableStateOf(getStatuses()) }

    fun checkAllGranted(): Boolean = permissions.all { perm ->
        val isIgnored = perm.getIgnore() == PlatformIgnore.IOS
        val isOutOfSdk = (perm.minSdk?.let { currentIosVersion < it } ?: false) ||
                (perm.maxSdk?.let { currentIosVersion > it } ?: false)
        val unavailable = !(serviceAvailableMap[perm] ?: false)

        when {
            isIgnored || isOutOfSdk -> true
            unavailable -> false
            else -> stateMap[perm.name] == PermissionStatus.Granted
        }
    }

    LaunchedEffect(stateMap) {
        onPermissionsResult(checkAllGranted())
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
                if (filtered.isEmpty()) {
                    onPermissionsResult(true)
                    return
                }
                coroutineScope.launch {
                    requestPermissionsSequentially(filtered) { allGranted ->
                        stateMap = getStatuses()
                        onPermissionsResult(allGranted)
                    }
                }
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatus() {
                val newStatus = permission.getPermissionStatus()
                if (newStatus != status) {
                    status = newStatus
                    onPermissionsResult(checkAllGranted())
                }
            }
        }
    }

    return actualStates + ignoredStates
}
