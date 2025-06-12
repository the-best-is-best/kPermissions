package io.github.kPermissionsGallery

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionType
import io.github.kpermissionsCore.PlatformIgnore

internal expect fun permissionRequest(): ((Boolean) -> Unit) -> Unit

internal expect fun registerIosProvider()

object GalleryPermission : Permission {

    init {
        registerIosProvider()
    }

    override val name: String
        get() = "gallery"
    override val type: PermissionType
        get() = PermissionType.Gallery
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = permissionRequest()
    private var _ignore: PlatformIgnore = PlatformIgnore.None
    override val ignore: PlatformIgnore
        get() = _ignore

    override fun changeIgnore(value: PlatformIgnore) {
        _ignore = value
    }

}