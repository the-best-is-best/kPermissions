package io.github.kPermissionsReadAudio

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.AndroidRememberPermissionStateCore
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PlatformIgnore

@Composable
internal actual fun AudioPermissionState(
    permission: ReadAudioPermission,
    onResult: (Boolean) -> Unit,
): PermissionState {
    return AndroidRememberPermissionStateCore(
        permission = permission,
        androidPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_AUDIO else null,
        onResult = onResult
    )
}

actual fun ReadAudioPermission.register(ignore: PlatformIgnore) {

    if (isAudioPermissionRegistered) return
    isAudioPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        ReadAudioPermission::class
    ) { permission, onResult ->
        AudioPermissionState(permission as ReadAudioPermission, onResult)
    }
}