package io.github.kPermissionsAudio

import android.Manifest
import android.os.Build
import io.github.kPermissions_api.Permission
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object ReadAudioPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "read_audio"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO else null


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

    override fun isServiceAvailable(): Boolean {
        return true
    }





}