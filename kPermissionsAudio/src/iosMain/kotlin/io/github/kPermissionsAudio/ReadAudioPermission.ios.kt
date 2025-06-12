package io.github.kPermissionsAudio

import androidx.compose.runtime.Composable
import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState
import io.github.kpermissionsCore.PermissionStatus
import io.github.kpermissionsCore.PlatformIgnore
import kotlin.experimental.ExperimentalObjCName


@Composable
internal actual fun ReadAudioPermissionState(
    permission: ReadAudioPermission,
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
@ObjCName("registerReadAudioPermission")
actual fun ReadAudioPermission.register(ignore: PlatformIgnore) {
    if (isReadAudioPermissionRegistered) return
    isReadAudioPermissionRegistered = true

    setIgnore(ignore)

    io.github.kpermissionsCore.PermissionRegistryInternal.registerPermissionProvider(
        ReadAudioPermission::class
    ) { permission, onResult ->
        ReadAudioPermissionState(permission as ReadAudioPermission, onResult)
    }

}