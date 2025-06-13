package io.github.kPermissionsAudio

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore


actual object ReadAudioPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "readAudio"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }
}