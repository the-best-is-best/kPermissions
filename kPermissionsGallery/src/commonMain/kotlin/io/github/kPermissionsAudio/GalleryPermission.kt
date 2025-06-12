package io.github.kPermissionsAudio

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

object GalleryPermission : Permission {
    override val name: String
        get() = "gallery"

    private var _ignore: PlatformIgnore = PlatformIgnore.None

    override val ignore: PlatformIgnore
        get() = _ignore

    internal fun setIgnore(value: PlatformIgnore) {
        _ignore = value
    }
}


@Composable
internal expect fun GalleryPermissionState(
    permission: GalleryPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isGalleryPermissionRegistered = false


expect fun GalleryPermission.register(ignore: PlatformIgnore = PlatformIgnore.None)

