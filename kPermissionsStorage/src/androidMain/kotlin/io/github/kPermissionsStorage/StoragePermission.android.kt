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

    override val name: String
        get() = "write_storage"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) Manifest.permission.WRITE_EXTERNAL_STORAGE else null
    override var minSdk: Int? = null
    override var maxSdk: Int? = null


}

actual object ReadStoragePermission : Permission {
    override val name: String
        get() = "read_storage"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_EXTERNAL_STORAGE else null
    override var minSdk: Int? = null
    override var maxSdk: Int? = null


}