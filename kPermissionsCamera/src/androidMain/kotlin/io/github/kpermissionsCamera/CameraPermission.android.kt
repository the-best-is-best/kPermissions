package io.github.kpermissionsCamera

import android.Manifest
import io.github.kPermissions_api.Permission


actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"
    override val androidPermissionName: String
        get() = Manifest.permission.CAMERA
    override var minSdk: Int? = null
    override var maxSdk: Int? = null


}