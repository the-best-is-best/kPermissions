package io.github.kpermissionslocationAlways

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import io.github.kPermissions_api.AndroidPermission
import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus

actual object LocationAlwaysPermission : Permission {
    override val name: String
        get() = "location_always"

    override val androidPermissionName: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            null
        }

    private var _minSdk: Int? = Build.VERSION_CODES.Q
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
        val locationManager =
            AppContextProvider.appContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true ||
                locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
    }

    fun openServiceSettings() {
        val context = AppContextProvider.appContext
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    override suspend fun refreshStatus(): PermissionStatus {
        val context = AppContextProvider.appContext

        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            androidPermissionName!!
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (granted) {
            return PermissionStatus.Granted
        }

        val showRationale = androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
            AndroidPermission.getActivity() ?: return PermissionStatus.Unavailable,
            androidPermissionName!!
        )

        return if (showRationale) {
            PermissionStatus.Denied
        } else {
            PermissionStatus.DeniedPermanently
        }
    }
}
