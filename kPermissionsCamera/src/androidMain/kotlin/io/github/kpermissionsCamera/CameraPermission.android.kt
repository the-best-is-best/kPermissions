package io.github.kpermissionsCamera

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore


actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"

    override val type: PermissionType
        get() = PermissionType.Camera

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override var ignore: PlatformIgnore = PlatformIgnore.None

}