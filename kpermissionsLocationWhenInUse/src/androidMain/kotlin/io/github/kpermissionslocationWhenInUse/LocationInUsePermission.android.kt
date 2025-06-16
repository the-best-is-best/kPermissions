package io.github.kpermissionslocationWhenInUse

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import io.github.kPermissions_api.Permission

actual object LocationInUsePermission : Permission {
    override val name: String
        get() = "location_in_use"

    @Suppress("RedundantNullableReturnType")
    override val androidPermissionName: String?
        get() = android.Manifest.permission.ACCESS_FINE_LOCATION


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

    override suspend fun isServiceAvailable(): Boolean {
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



}