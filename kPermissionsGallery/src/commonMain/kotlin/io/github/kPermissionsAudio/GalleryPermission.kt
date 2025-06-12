package io.github.kPermissionsAudio

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PlatformIgnore

expect object GalleryPermission : Permission {
    override val name: String

    override val permissionRequest: ((Boolean) -> Unit) -> Unit

    override var ignore: PlatformIgnore

}
