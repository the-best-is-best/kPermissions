package io.github.kpermissionsCamera

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.github.kPermissions_api.AndroidPermission
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus


actual object CameraPermission : Permission {
    override val name: String
        get() = "camera"

    @Suppress("RedundantNullableReturnType")
    override val androidPermissionName: String?
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

    override suspend fun refreshStatus(): PermissionStatus {
        val sdkInt = android.os.Build.VERSION.SDK_INT

        if (androidPermissionName == null ||
            (minSdk != null && sdkInt < minSdk!!) ||
            (maxSdk != null && sdkInt > maxSdk!!)
        ) {
            return PermissionStatus.Granted
        }

        val context = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable

        val granted = ContextCompat.checkSelfPermission(
            context,
            androidPermissionName!!
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            return PermissionStatus.Granted
        }

        val activity = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable

        val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName!!)

        return if (showRationale) {
            PermissionStatus.Denied
        } else {
            PermissionStatus.DeniedPermanently
        }
    }


}