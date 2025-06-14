package io.github.kPermissionsVideo

import android.Manifest
import android.os.Build
import io.github.kPermissions_api.AndroidPermission
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kpermissions_cmp.PlatformIgnore
import io.github.kpermissions_cmp.setIgnore

actual object ReadVideoPermission : Permission {
    init {
        this.setIgnore(PlatformIgnore.IOS)
    }

    override val name: String
        get() = "read_video"
    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_VIDEO else null

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
        return true
    }

    override suspend fun refreshStatus(): PermissionStatus {
        val activity = AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable
        val sdkInt = Build.VERSION.SDK_INT

        // Check permission not required for current SDK
        if (androidPermissionName == null ||
            (minSdk != null && sdkInt < minSdk!!) ||
            (maxSdk != null && sdkInt > maxSdk!!)
        ) {
            return PermissionStatus.Granted
        }

        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
            activity,
            androidPermissionName!!
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (granted) {
            return PermissionStatus.Granted
        }

        val showRationale = androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            androidPermissionName!!
        )

        return if (showRationale) {
            PermissionStatus.Denied
        } else {
            PermissionStatus.DeniedPermanently
        }
    }


}