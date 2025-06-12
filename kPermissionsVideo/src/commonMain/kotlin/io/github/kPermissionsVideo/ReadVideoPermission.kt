package io.github.kPermissionsVideo

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PlatformIgnore

object ReadVideoPermission : Permission {
    override val name: String
        get() = "read_video"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override val type: io.github.kpermissionsCore.PermissionType
        get() = io.github.kpermissionsCore.PermissionType.ReadVideo


    override var ignore: PlatformIgnore = PlatformIgnore.IOS
}

