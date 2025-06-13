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


    override var minSdk: Int? = null
    override var maxSdk: Int? = null


}