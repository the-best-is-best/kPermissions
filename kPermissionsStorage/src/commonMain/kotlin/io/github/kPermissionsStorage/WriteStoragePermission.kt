package io.github.kPermissionsStorage

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState

object WriteStoragePermission : Permission {
    override val name: String
        get() = "write_storage"
}

object ReadStoragePermission : Permission {
    override val name: String
        get() = "read_storage"
}


@Composable
internal expect fun WriteStoragePermissionState(
    permission: WriteStoragePermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isWriteStoragePermissionRegistered = false


expect fun WriteStoragePermission.register()


@Composable
internal expect fun ReadStoragePermissionState(
    permission: ReadStoragePermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isReadStoragePermissionRegistered = false


expect fun ReadStoragePermission.register()