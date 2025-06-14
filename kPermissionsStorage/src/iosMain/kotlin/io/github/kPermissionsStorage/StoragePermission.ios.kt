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

    override fun isServiceAvailable(): Boolean {
        return true
    }

    override fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null

    override val minSdk: Int?
        get() = _minSdk

    override val maxSdk: Int?
        get() = _maxSdk


    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override fun refreshStatus(): PermissionStatus {
        return getPermissionStatus()
    }



}

actual object ReadStoragePermission : Permission {
    override val name: String
        get() = "read_storage"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    override fun isServiceAvailable(): Boolean {
        return true
    }

    override fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null

    override val minSdk: Int?
        get() = _minSdk

    override val maxSdk: Int?
        get() = _maxSdk


    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override fun refreshStatus(): PermissionStatus {
        return getPermissionStatus()
    }


}