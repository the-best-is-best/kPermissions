package io.github.kPermissionsAudio

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore

actual object GalleryPermission : Permission {
    actual override val name: String
        get() = "gallery"
    actual override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = {}
    override val type: PermissionType
        get() = PermissionType.Gallery
    actual override var ignore: PlatformIgnore = PlatformIgnore.None


}