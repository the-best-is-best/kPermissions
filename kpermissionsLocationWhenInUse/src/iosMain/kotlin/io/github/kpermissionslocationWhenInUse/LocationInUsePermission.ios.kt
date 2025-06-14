package io.github.kpermissionslocationWhenInUse

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

actual object LocationInUsePermission : Permission {

    override val name: String
        get() = "location_in_use"

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
        return CLLocationManager.locationServicesEnabled()
    }


    override fun getPermissionStatus(): PermissionStatus {
        return when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways
                -> PermissionStatus.Granted

            kCLAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
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

    override fun refreshStatus(): PermissionStatus {
        return getPermissionStatus()
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
