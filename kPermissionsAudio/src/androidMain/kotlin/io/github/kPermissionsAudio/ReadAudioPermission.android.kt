package io.github.kPermissionsAudio

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionType
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object ReadAudioPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "read_audio"
    override val type: PermissionType
        get() = PermissionType.ReadAudio

}