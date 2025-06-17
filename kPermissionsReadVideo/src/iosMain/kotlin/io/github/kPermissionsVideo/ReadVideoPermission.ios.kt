package io.github.kPermissionsVideo

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object ReadVideoPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    actual override val name: String
        get() = "read_video"
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}

    actual override suspend fun isServiceAvailable(): Boolean {
        return true
    }

    override suspend fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null

    actual override val minSdk: Int?
        get() = _minSdk

    actual override val maxSdk: Int?
        get() = _maxSdk


    actual override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }



}