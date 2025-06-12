package io.github.kPermissionsVideo

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

@Composable
internal actual fun ReadVideoPermissionState(
    permission: ReadVideoPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else null,
        onResult = onResult
    )
}

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