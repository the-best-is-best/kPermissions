package io.github.kPermissionsStorage

import android.Manifest
import android.os.Build
import io.github.kPermissions_api.Permission
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object WriteStoragePermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    actual override val name: String
        get() = "write_storage"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) Manifest.permission.WRITE_EXTERNAL_STORAGE else null

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

    actual override suspend fun isServiceAvailable(): Boolean {
        return true
    }


}

actual object ReadStoragePermission : Permission {
    actual override val name: String
        get() = "read_storage"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_EXTERNAL_STORAGE else null

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

    actual override suspend fun isServiceAvailable(): Boolean {
        return true
    }


}