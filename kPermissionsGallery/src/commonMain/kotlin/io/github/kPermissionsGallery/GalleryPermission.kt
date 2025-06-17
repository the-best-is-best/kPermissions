package io.github.kPermissionsGallery

import io.github.kPermissions_api.Permission


expect object GalleryPermission : Permission {
    override val maxSdk: Int?
    override val minSdk: Int?
    override val name: String
    override suspend fun isServiceAvailable(): Boolean
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)

}