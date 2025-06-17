package io.github.kpermissionsCamera

import android.Manifest
import android.content.pm.PackageManager
import io.github.kPermissions_api.Permission


actual object CameraPermission : Permission {
    actual override val name: String
        get() = "camera"

    @Suppress("RedundantNullableReturnType")
    override val androidPermissionName: String?
        get() = Manifest.permission.CAMERA

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

        return AppContextProvider.appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}