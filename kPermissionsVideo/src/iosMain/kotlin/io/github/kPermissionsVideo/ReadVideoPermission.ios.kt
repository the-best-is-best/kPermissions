package io.github.kPermissionsVideo

import io.github.kPermissions_api.Permission
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object ReadVideoPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "read_video"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
}