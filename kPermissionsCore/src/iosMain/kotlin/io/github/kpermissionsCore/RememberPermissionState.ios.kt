package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
actual fun RequestPermission(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit
): PermissionState {
    if (permission.ignore == PlatformIgnore.IOS) {
        onPermissionResult(true)
        return object : PermissionState {
            override val permission: Permission = permission
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
        }
    }

    fun getStatus() = PermissionStatusRegistry.getStatus(permission.name)

    var stateValue by remember { mutableStateOf(getStatus()) }

    LaunchedEffect(Unit) {
        onPermissionResult(getStatus() == PermissionStatus.Granted)
    }
    OnAppResumed {
        val newStatus = getStatus()
        if (newStatus != stateValue) {
            stateValue = newStatus
            onPermissionResult(getStatus() == PermissionStatus.Granted)
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
                status = if (granted) PermissionStatus.Granted else PermissionStatus.Denied
                onPermissionResult(granted)
            }
        }

        override fun openAppSettings() {
            openAppSettingsPlatform()
        }
    }

}

@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
    onPermissionsResult: (Boolean) -> Unit
): List<PermissionState> {
    val filtered = permissions.filter { it.ignore != PlatformIgnore.IOS }

    fun getStatuses(): Map<String, PermissionStatus> =
        filtered.associate { it.name to PermissionStatusRegistry.getStatus(it.name) }

    var stateMap by remember { mutableStateOf(getStatuses()) }

    // تحديث onPermissionsResult عند بداية الـ Composable
    LaunchedEffect(Unit) {
        val allGranted = stateMap.values.all { it == PermissionStatus.Granted }
        onPermissionsResult(allGranted)
    }

    // مراقبة التطبيق عند العودة من الخلفية (لرصد تغيّر الحالة)
    OnAppResumed {
        val newStatuses = getStatuses()
        if (newStatuses != stateMap) {
            stateMap = newStatuses
            val allGranted = newStatuses.values.all { it == PermissionStatus.Granted }
            onPermissionsResult(allGranted)
        }
    }

    return permissions.map { permission ->
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
                    val allGranted = stateMap.values.all { it == PermissionStatus.Granted }
                    onPermissionsResult(allGranted)
                }
            }

            override fun openAppSettings() {
                openAppSettingsPlatform()
            }
        }
    }
}
