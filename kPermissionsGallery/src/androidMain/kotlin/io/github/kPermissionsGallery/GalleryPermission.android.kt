package io.github.kPermissionsGallery

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionType


actual object GalleryPermission : Permission {
   override val name: String
      get() = "gallery"
   override val type: PermissionType
      get() = PermissionType.Gallery

}