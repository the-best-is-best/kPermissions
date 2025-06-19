package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import kotlinx.coroutines.launch

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

    // تصفية الصلاحيات المناسبة للأندرويد وحسب SDK
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

    val currentStatusMap = remember { mutableStateMapOf<Permission, PermissionStatus>() }


    // بناء PermissionState لكل صلاحية
    val permissionStates = filteredPermissions.map { perm ->

        object : PermissionState {
            override val permission = perm

            override val status: PermissionStatus
                get() = currentStatusMap[perm] ?: PermissionStatus.Denied

            override fun launchPermissionRequest() {
                // التعامل مع طلب الصلاحيات يتم دفعة واحدة من الخارج
            }

            override fun openAppSettings() = openAppSettingsPlatform()

            override suspend fun refreshStatus() {
                permission.refreshState()
                val getAccStatus =
                    multipleLauncher.permissions.find { it.permission == perm.androidPermissionName }?.status
                val status = when (getAccStatus) {
                    is com.google.accompanist.permissions.PermissionStatus.Granted ->
                        if (perm.isServiceAvailable()) PermissionStatus.Granted else PermissionStatus.Unavailable

                    is com.google.accompanist.permissions.PermissionStatus.Denied ->
                        if (getAccStatus.shouldShowRationale) PermissionStatus.Denied else PermissionStatus.DeniedPermanently

                    else -> PermissionStatus.Denied
                }
                currentStatusMap[perm] = status
            }
        }
    }

    val grantedPermissions = (permissions - filteredPermissions.toSet()).map { grantedState(it) }
    val allPermissionStates = remember(permissionStates, grantedPermissions) {
        permissionStates + grantedPermissions
    }

    var statusesSnapshot by remember { mutableStateOf(allPermissionStates.map { it.status }) }

    // تحديث الحالة فور بدء الـ Composable (يعني أول ما تدخل الشاشة)


    var isFirstRun by remember { mutableStateOf(true) }
    var requested by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    suspend fun refreshAllStatuses() {
        filteredPermissions.forEach { perm ->
            perm.refreshState()
            val accStatus =
                multipleLauncher.permissions.find { it.permission == perm.androidPermissionName }?.status

            val status = when (accStatus) {
                is com.google.accompanist.permissions.PermissionStatus.Granted ->
                    if (perm.isServiceAvailable()) PermissionStatus.Granted else PermissionStatus.Unavailable

                is com.google.accompanist.permissions.PermissionStatus.Denied ->
                    if (isFirstRun) PermissionStatus.Denied
                    else if (accStatus.shouldShowRationale) PermissionStatus.Denied
                    else PermissionStatus.DeniedPermanently

                else -> PermissionStatus.Denied
            }

            currentStatusMap[perm] = status
        }

        statusesSnapshot = allPermissionStates.map { it.status }
    }


    SideEffect {
        if (isFirstRun && requested) {
            requested = true
            isFirstRun = false
            scope.launch {
                refreshAllStatuses()
            }
        }
    }
    LaunchedEffect(!isFirstRun) {
        snapshotFlow { multipleLauncher.permissions.map { it.status } }
            .collect {
                refreshAllStatuses()
            }
    }

    return remember(allPermissionStates) {
        object : MultiPermissionState {
            override val permissions = allPermissionStates.map { it.permission }

            override val statuses: List<PermissionStatus>
                get() = statusesSnapshot

            override fun launchPermissionsRequest() {
                requested = true
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
