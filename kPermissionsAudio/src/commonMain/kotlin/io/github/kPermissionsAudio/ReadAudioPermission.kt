package io.github.kPermissionsAudio

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore

object ReadAudioPermission : Permission {
    override val name: String
        get() = "read_audio"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override val type: PermissionType
        get() = PermissionType.ReadAudio

    override var ignore: PlatformIgnore = PlatformIgnore.None

}