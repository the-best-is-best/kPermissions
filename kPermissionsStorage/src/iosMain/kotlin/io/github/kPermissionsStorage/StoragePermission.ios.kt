package io.github.kPermissionsStorage

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PlatformIgnore
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("registerWriteStoragePermission")
actual fun WriteStoragePermission.register(ignore: PlatformIgnore) {
    if (isWriteStoragePermissionRegistered) return
    isWriteStoragePermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        WriteStoragePermission::class
    ) { permission, onResult ->
        WriteStoragePermissionState(permission as WriteStoragePermission, onResult)
    }
}

@Composable
internal actual fun WriteStoragePermissionState(
    permission: WriteStoragePermission,
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
@ObjCName("registerReadStoragePermission")
@Composable
internal actual fun ReadStoragePermissionState(
    permission: ReadStoragePermission,
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
@ObjCName("registerReadStoragePermission")
actual fun ReadStoragePermission.register(ignore: PlatformIgnore) {
    if (isReadStoragePermissionRegistered) return
    isReadStoragePermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        ReadStoragePermission::class
    ) { permission, onResult ->
        ReadStoragePermissionState(permission as ReadStoragePermission, onResult)
    }
}