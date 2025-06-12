package io.github.kPermissionsVideo

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PlatformIgnore
import kotlin.experimental.ExperimentalObjCName


@Composable
internal actual fun ReadVideoPermissionState(
    permission: ReadVideoPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    onResult(true)
    return object : PermissionState {
        override val permission: Permission = permission
        override var status: PermissionStatus = PermissionStatus.Granted
        override fun launchPermissionRequest() {}
        override fun openAppSettings() {}
    }

}

@OptIn(ExperimentalObjCName::class)
@ObjCName("registerReadVideoPermission")
actual fun ReadVideoPermission.register(ignore: PlatformIgnore) {
    if (isAReadVideoPermissionRegistered) return
    isAReadVideoPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        ReadVideoPermission::class
    ) { permission, onResult ->
        ReadVideoPermissionState(permission as ReadVideoPermission, onResult)
    }

}