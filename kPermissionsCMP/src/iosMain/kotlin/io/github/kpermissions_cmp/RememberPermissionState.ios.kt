package io.github.kpermissions_cmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import io.github.kPermissions_api.MultiPermissionState
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissionsCore.IOSPermissionManager
import io.github.kpermissionsCore.IOSPermissionManager.canRequest
import io.github.kpermissionsCore.currentIosVersion
import io.github.kpermissionsCore.openAppSettingsPlatform
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch


@Composable
actual fun RequestPermission(
    permission: Permission,
): PermissionState {
    val isIgnored = permission.getIgnore() == PlatformIgnore.IOS
    val isOutOfSdk = (permission.minSdk?.let { IOSPermissionManager.currentIosVersion < it } ?: false) ||
            (permission.maxSdk?.let { IOSPermissionManager.currentIosVersion > it } ?: false)

    val fixedStatus = when {
        isIgnored || isOutOfSdk -> PermissionStatus.Granted
        else -> null
    }

    val coroutineScope = rememberCoroutineScope()


    if (fixedStatus != null) {

        return object : PermissionState {
            override val permission = permission
            override var status: PermissionStatus = fixedStatus
            override fun launchPermissionRequest() {}
            override fun openAppSettings() = Unit
            override suspend fun refreshStatus() {

            }
        }
    }

    var stateValue by remember { mutableStateOf<PermissionStatus>(PermissionStatus.Denied) }

    OnAppResumed {
        coroutineScope.launch {
            val newStatus = IOSPermissionManager.getStatus(permission)
            if (newStatus != stateValue) {
                stateValue = newStatus
            }
        }
    }
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
                    coroutineScope.launch {
                        getRefreshStatus()
                    }
                }
            }
        }

        override fun openAppSettings() = openAppSettingsPlatform()

        override suspend fun refreshStatus() {
            getRefreshStatus()
        }

        private suspend fun getRefreshStatus() {
            val newStatus = IOSPermissionManager.getStatus(permission)

            stateValue = newStatus

        }
    }

}

@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
): MultiPermissionState {
    val coroutineScope = rememberCoroutineScope()

    val filtered = permissions.filter { perm ->
        perm.getIgnore() != PlatformIgnore.IOS &&
                (perm.minSdk?.let { IOSPermissionManager.currentIosVersion >= it } ?: true) &&
                (perm.maxSdk?.let { IOSPermissionManager.currentIosVersion <= it } ?: true)
    }

    val ignoredPermissions = permissions - filtered.toSet()

    val ignoredStates = ignoredPermissions.map { perm ->
        object : PermissionState {
            override val permission: Permission = perm
            override var status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
            override fun openAppSettings() {}
            override suspend fun refreshStatus() {}
        }
    }

    var stateMap by remember {
        mutableStateOf<Map<String, PermissionStatus>>(
            filtered.associate { it.name to PermissionStatus.NotDeclared }
        )
    }

    LaunchedEffect(Unit) {
        val statuses = filtered.associate { it.name to IOSPermissionManager.getStatus(it) }
        stateMap = statuses
    }

    var serviceAvailableMap by remember { mutableStateOf<Map<Permission, Boolean>>(emptyMap()) }
    LaunchedEffect(permissions) {
        val results = permissions.associateWith { it.isServiceAvailable() }
        serviceAvailableMap = results
    }

    OnAppResumed {
        coroutineScope.launch {
            val newStatuses = filtered.associate { it.name to IOSPermissionManager.getStatus(it) }
            stateMap = newStatuses
        }
    }

    val actualStates = filtered.map { permission ->
        val statusState = derivedStateOf {
            stateMap[permission.name] ?: PermissionStatus.Denied
        }

        object : PermissionState {
            override val permission: Permission = permission

            override var status: PermissionStatus
                get() = statusState.value
                set(value) {
                    stateMap = stateMap.toMutableMap().apply {
                        this[permission.name] = value
                    }
                }

            override fun launchPermissionRequest() {
                coroutineScope.launch {
                    val updated = stateMap.toMutableMap()

                    for (p in filtered) {
                        if (updated[p.name]?.canRequest == true) {
                            val completable = CompletableDeferred<Unit>()
                            p.permissionRequest {
                                coroutineScope.launch {
                                    updated[p.name] = IOSPermissionManager.getStatus(p)
                                    completable.complete(Unit)
                                }
                            }
                            completable.await()
                        } else {
                            updated[p.name] = IOSPermissionManager.getStatus(p)
                        }
                    }

                    stateMap = updated
                }
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatus() {
                val newStatus = IOSPermissionManager.getStatus(permission)
                if (newStatus != status) {
                    status = newStatus
                }
            }
        }
    }

    val allStates = actualStates + ignoredStates

    return remember(allStates, stateMap) {
        object : MultiPermissionState {
            override val permissions: List<Permission> = allStates.map { it.permission }
            override val statuses: List<PermissionStatus> = allStates.map { it.status }

            override fun launchPermissionsRequest() {
                allStates.forEach { it.launchPermissionRequest() }
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatuses() {
                allStates.forEach { it.refreshStatus() }
            }
        }
    }
}
