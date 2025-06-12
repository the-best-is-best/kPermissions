package io.github.kPermissionsAudio

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

object ReadAudioPermission : Permission {
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
internal expect fun ReadAudioPermissionState(
    permission: ReadAudioPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isReadAudioPermissionRegistered = false


expect fun ReadAudioPermission.register(ignore: PlatformIgnore = PlatformIgnore.None)

