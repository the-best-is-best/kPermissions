package io.github.kPermissionsStorage

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object WriteStoragePermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "write_storage"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }

    override var minSdk: Int? = null
    override var maxSdk: Int? = null
}

actual object ReadStoragePermission : Permission {
    override val name: String
        get() = "read_storage"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }

    override var minSdk: Int? = null
    override var maxSdk: Int? = null
}