package io.github.kpermissionsCamera

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionType


actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"
    override val type: PermissionType
        get() = PermissionType.Camera


}