package io.github.kPermissionsVideo

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

object ReadVideoPermission : Permission {
    override val name: String
        get() = "audio"

    private var _ignore: PlatformIgnore = PlatformIgnore.None

    override val ignore: PlatformIgnore
        get() = _ignore

    internal fun setIgnore(value: PlatformIgnore) {
        _ignore = value
    }
}


@Composable
internal expect fun ReadVideoPermissionState(
    permission: ReadVideoPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isAReadVideoPermissionRegistered = false


expect fun ReadVideoPermission.register(ignore: PlatformIgnore = PlatformIgnore.None)

