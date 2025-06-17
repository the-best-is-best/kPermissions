package io.github.kPermissionsGallery

import android.Manifest
import android.os.Build
import io.github.kPermissions_api.Permission


actual object GalleryPermission : Permission {
    actual override val name: String
      get() = "gallery"
   override val androidPermissionName: String?
      get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else null

   private var _minSdk: Int? = null
   private var _maxSdk: Int? = null

    actual override val minSdk: Int?
      get() = _minSdk

    actual override val maxSdk: Int?
      get() = _maxSdk


    actual override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
      _minSdk = minSdk
      _maxSdk = maxSdk
   }

    actual override suspend fun isServiceAvailable(): Boolean {
      return true
   }




}