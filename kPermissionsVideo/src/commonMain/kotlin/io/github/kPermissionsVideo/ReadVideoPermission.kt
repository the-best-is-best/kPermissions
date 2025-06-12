package io.github.kPermissionsVideo

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PlatformIgnore

object ReadVideoPermission : Permission {

    override val name: String
        get() = "read_video"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override val ignore: PlatformIgnore
        get() = PlatformIgnore.IOS

    override fun changeIgnore(value: PlatformIgnore) {
        throw Exception("ReadVideoPermission does not support changing ignore state")
    }

    override val type: io.github.kpermissionsCore.PermissionType
        get() = io.github.kpermissionsCore.PermissionType.ReadVideo


}

