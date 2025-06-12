package io.github.kPermissionsAudio

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore

object ReadAudioPermission : Permission {
    override val name: String
        get() = "read_audio"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override val ignore: PlatformIgnore
        get() = PlatformIgnore.IOS

    override fun changeIgnore(value: PlatformIgnore) {
        throw Exception("ReadAudioPermission does not support changing ignore state")
    }

    override val type: PermissionType
        get() = PermissionType.ReadAudio


}