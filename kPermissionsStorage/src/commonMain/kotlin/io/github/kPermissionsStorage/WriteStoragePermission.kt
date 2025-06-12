package io.github.kPermissionsStorage

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

object WriteStoragePermission : Permission {
    override val name: String
        get() = "write_storage"

    private var _ignore: PlatformIgnore = PlatformIgnore.IOS

    override val ignore: PlatformIgnore
        get() = _ignore

    internal fun setIgnore(value: PlatformIgnore) {
        _ignore = value
    }
}

object ReadStoragePermission : Permission {
    override val name: String
        get() = "read_storage"

    private var _ignore: PlatformIgnore = PlatformIgnore.IOS

    override val ignore: PlatformIgnore
        get() = _ignore

    internal fun setIgnore(value: PlatformIgnore) {
        _ignore = value
    }
}


@Composable
internal expect fun WriteStoragePermissionState(
    permission: WriteStoragePermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isWriteStoragePermissionRegistered = false


expect fun WriteStoragePermission.register(ignore: PlatformIgnore = PlatformIgnore.IOS)


@Composable
internal expect fun ReadStoragePermissionState(
    permission: ReadStoragePermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isReadStoragePermissionRegistered = false


expect fun ReadStoragePermission.register(ignore: PlatformIgnore = PlatformIgnore.IOS)