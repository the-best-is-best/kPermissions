package io.github.kPermissionsStorage

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore

object WriteStoragePermission : Permission {

    override val name: String
        get() = "write_storage"

    override val type: PermissionType
        get() = PermissionType.WriteStorage

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override val ignore: PlatformIgnore
        get() = PlatformIgnore.IOS

    override fun changeIgnore(value: PlatformIgnore) {
        throw Exception("WriteStoragePermission does not support changing ignore state")
    }


}

object ReadStoragePermission : Permission {

    override val name: String
        get() = "read_storage"

    override val type: PermissionType
        get() = PermissionType.ReadStorage


    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override val ignore: PlatformIgnore
        get() = PlatformIgnore.IOS

    override fun changeIgnore(value: PlatformIgnore) {
        throw Exception("ReadStoragePermission does not support changing ignore state")
    }


}

