package io.github.kpermissionsCamera

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

object CameraPermission : Permission {
    override val name: String
        get() = "camera"

    private var _ignore: PlatformIgnore = PlatformIgnore.None

    override val ignore: PlatformIgnore
        get() = _ignore

    internal fun setIgnore(value: PlatformIgnore) {
        _ignore = value
    }
}


@Composable
internal expect fun CameraPermissionState(
    permission: CameraPermission,
    onResult: (Boolean) -> Unit,
): PermissionState

internal var isCameraPermissionRegistered = false


expect fun CameraPermission.register(ignore: PlatformIgnore = PlatformIgnore.None)

