package io.github.kpermissionslocationWhenInUse

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

actual object LocationInUsePermission : Permission {

    actual override val name: String
        get() = "location_in_use"

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
        return withContext(Dispatchers.Default) {
            CLLocationManager.locationServicesEnabled()
        }
    }


    override suspend fun getPermissionStatus(): PermissionStatus =
        withContext(Dispatchers.Default) {
        if (!CLLocationManager.locationServicesEnabled()) {
            return@withContext PermissionStatus.DeniedPermanently
        }

            return@withContext when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> PermissionStatus.Granted

                kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently

                kCLAuthorizationStatusNotDetermined -> PermissionStatus.Denied

                else -> PermissionStatus.Denied
        }
    }


    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            val manager = CLLocationManager()
            val delegate = LocationPermissionDelegate(callback)
            manager.delegate = delegate
            manager.requestWhenInUseAuthorization()
        }
}

private class LocationPermissionDelegate(
    val callback: (Boolean) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        val status = CLLocationManager.authorizationStatus()
        callback(
            status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                    status == kCLAuthorizationStatusAuthorizedAlways
        )
    }
}
