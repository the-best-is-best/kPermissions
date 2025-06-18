package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.github.kPermissions_api.MultiPermissionState
import io.github.kPermissions_api.NotDeclared
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.Unavailable
import io.github.kPermissions_api.isDeclaredInManifest
import io.github.kPermissions_api.isDenied
import io.github.kPermissions_api.isDeniedPermanently
import io.github.kPermissions_api.isGranted
import io.github.kPermissions_api.refreshState
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.getIgnore
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestPermission(
    permission: Permission,
): PermissionState {
    val androidPermission = permission.androidPermissionName
    val currentSdk = android.os.Build.VERSION.SDK_INT

    if (androidPermission == null ||
        permission.getIgnore() == PlatformIgnore.Android ||
        (permission.minSdk != null && currentSdk < permission.minSdk!!) ||
        (permission.maxSdk != null && currentSdk > permission.maxSdk!!)
    ) {
        return grantedState(permission)
    }

    val launcher = rememberPermissionState(androidPermission)

    var shouldRequest by remember { mutableStateOf(true) }
    var listenToStatus by remember { mutableStateOf(true) }
    var isServiceActive by remember { mutableStateOf(true) }

    val accStatus = launcher.status

    LaunchedEffect(accStatus, shouldRequest) {
        listenToStatus = true
        isServiceActive =
            if (accStatus is com.google.accompanist.permissions.PermissionStatus.Granted) {
                permission.isServiceAvailable()
            } else {
                true
            }
    }
    LaunchedEffect(shouldRequest) {
        permission.refreshState()
        if (launcher.status is com.google.accompanist.permissions.PermissionStatus.Granted) {
            isServiceActive = permission.isServiceAvailable()
        }
    }

    var currentStatus by remember { mutableStateOf<PermissionStatus?>(null) }

    return object : PermissionState {
        override val permission = permission
        override val status: PermissionStatus
            get() {
                if (listenToStatus) {
                    if (!permission.isDeclaredInManifest()) return PermissionStatus.NotDeclared
                    val status = when (accStatus) {
                        com.google.accompanist.permissions.PermissionStatus.Granted -> {
                            if (isServiceActive) PermissionStatus.Granted else PermissionStatus.Unavailable
                        }
                        is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                            if (shouldRequest || accStatus.shouldShowRationale) {
                                PermissionStatus.Denied
                            } else {
                                PermissionStatus.DeniedPermanently
                            }
                        }
                    }
                    currentStatus = status
                }
                return currentStatus ?: PermissionStatus.Denied
            }

        override fun launchPermissionRequest() {
            listenToStatus = false
            launcher.launchPermissionRequest()
            shouldRequest = false

        }

        override fun openAppSettings() = openAppSettingsPlatform()

        override suspend fun refreshStatus() {
            permission.refreshState()
            if (launcher.status is com.google.accompanist.permissions.PermissionStatus.Granted) {
                isServiceActive = permission.isServiceAvailable()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestMultiPermissions(
    permissions: List<Permission>,
): MultiPermissionState {
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
    val multipleLauncher = rememberMultiplePermissionsState(androidPermissions)

    val listenToStatusMap = remember { mutableStateMapOf<Permission, Boolean>() }
    val shouldRequestMap = remember { mutableStateMapOf<Permission, Boolean>() }
    val serviceAvailabilityMap = remember { mutableStateMapOf<Permission, Boolean>() }

    val permissionStates = filteredPermissions.mapIndexed { index, perm ->
        if (!listenToStatusMap.containsKey(perm)) listenToStatusMap[perm] = true
        if (!shouldRequestMap.containsKey(perm)) shouldRequestMap[perm] = true

        val accStatus = multipleLauncher.permissions.getOrNull(index)?.status

        LaunchedEffect(accStatus, shouldRequestMap[perm]) {
            if (listenToStatusMap[perm] == true) {
                val granted =
                    accStatus is com.google.accompanist.permissions.PermissionStatus.Granted
                serviceAvailabilityMap[perm] = if (granted) {
                    perm.isServiceAvailable()
                } else true
            }
        }

        object : PermissionState {
            override val permission = perm

            override val status: PermissionStatus
                get() {
                    if (!perm.isDeclaredInManifest()) return PermissionStatus.NotDeclared

                    val acc = multipleLauncher.permissions.getOrNull(index)?.status
                        ?: return PermissionStatus.Denied

                    if (listenToStatusMap[perm] != true) return PermissionStatus.Denied

                    return when (acc) {
                        is com.google.accompanist.permissions.PermissionStatus.Granted ->
                            if (serviceAvailabilityMap[perm] == true) PermissionStatus.Granted
                            else PermissionStatus.Unavailable

                        is com.google.accompanist.permissions.PermissionStatus.Denied ->
                            if (shouldRequestMap[perm] == true || acc.shouldShowRationale)
                                PermissionStatus.Denied
                            else PermissionStatus.DeniedPermanently
                    }
                }

            override fun launchPermissionRequest() {
                // handled via batch request
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatus() {
                permission.refreshState()
                val getAccStatus = multipleLauncher.permissions.getOrNull(index)?.status
                if (getAccStatus is com.google.accompanist.permissions.PermissionStatus.Granted) {
                    serviceAvailabilityMap[perm] = perm.isServiceAvailable()
                }
            }

        }
    }

    val grantedPermissions = (permissions - filteredPermissions.toSet()).map { grantedState(it) }
    val allPermissionStates = remember(permissionStates, grantedPermissions) {
        permissionStates + grantedPermissions
    }

    var statusesSnapshot by remember { mutableStateOf(allPermissionStates.map { it.status }) }
    var requestTriggered by remember { mutableStateOf(false) }

    // تحديث snapshot عند أي تغيير في permission state
    LaunchedEffect(multipleLauncher.permissions) {
        snapshotFlow { multipleLauncher.permissions.map { it.status } }
            .collectLatest {
                filteredPermissions.forEach { perm ->
                    listenToStatusMap[perm] = true
                }
                statusesSnapshot = allPermissionStates.map { it.status }
            }
    }

    return remember(allPermissionStates) {
        object : MultiPermissionState {
            override val permissions = allPermissionStates.map { it.permission }

            override val statuses: List<PermissionStatus>
                get() = statusesSnapshot

            override fun launchPermissionsRequest() {
                filteredPermissions.forEach { perm ->
                    listenToStatusMap[perm] = false
                    shouldRequestMap[perm] = false
                }
                requestTriggered = true
                multipleLauncher.launchMultiplePermissionRequest()
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatuses() {
                allPermissionStates.forEach { it.refreshStatus() }
                statusesSnapshot = allPermissionStates.map { it.status }
            }

            override fun allPermissionsGranted() = statusesSnapshot.all { it.isGranted }

            override fun anyPermissionDenied() = statusesSnapshot.any { it.isDenied }

            override fun anyPermissionDeniedPermanently() =
                statusesSnapshot.any { it.isDeniedPermanently }

            override fun anyPermissionUnavailable() = statusesSnapshot.any { it.Unavailable }

            override fun anyPermissionNotDeclared() = statusesSnapshot.any { it.NotDeclared }
        }
    }
}


@Composable
private fun grantedState(permission: Permission): PermissionState = object : PermissionState {
    override val permission = permission
    override val status = PermissionStatus.Granted
    override fun launchPermissionRequest() {}
    override fun openAppSettings() = openAppSettingsPlatform()
    override suspend fun refreshStatus() {
        permission.refreshState()
    }
}
