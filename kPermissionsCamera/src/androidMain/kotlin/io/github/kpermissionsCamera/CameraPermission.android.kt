package io.github.kpermissionsCamera

import android.Manifest
import android.content.pm.PackageManager
import io.github.kPermissions_api.Permission


actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"
    override val androidPermissionName: String
        get() = Manifest.permission.CAMERA

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null

    override val minSdk: Int?
        get() = _minSdk

    override val maxSdk: Int?
        get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override fun isServiceAvailable(): Boolean {

        return AppContextProvider.appContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

}