package io.github.kPermissionsStorage

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState

actual fun WriteStoragePermission.register() {
    if (isWriteStoragePermissionRegistered) return
    isWriteStoragePermissionRegistered = true

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
    val androidPermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    } else {
        null
    }

    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = androidPermission,
        onResult = onResult,

        )
}


@Composable
internal actual fun ReadStoragePermissionState(
    permission: ReadStoragePermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    val androidPermission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }

    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = androidPermission,
        onResult = onResult,

        )
}

actual fun ReadStoragePermission.register() {
    if (isReadStoragePermissionRegistered) return
    isReadStoragePermissionRegistered = true

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        ReadStoragePermission::class
    ) { permission, onResult ->
        ReadStoragePermissionState(permission as ReadStoragePermission, onResult)
    }
}