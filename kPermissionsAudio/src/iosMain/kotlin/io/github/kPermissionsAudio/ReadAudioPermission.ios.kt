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

    override fun checkPermissionStatus(): PermissionStatus {
        return PermissionStatus.Granted
    }


}