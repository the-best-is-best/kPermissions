package io.github.kpermissionslocationAlways

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

actual object LocationAlwaysPermission : Permission {

    override val name: String
        get() = "location_always"

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
        val status = when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusAuthorizedAlways -> PermissionStatus.Granted

            kCLAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusNotDetermined -> PermissionStatus.Denied
            else -> PermissionStatus.Denied
        }

        println("🔍 iOS Location Permission Status: $status")
        return status
    }

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            val manager = CLLocationManager()
            val delegate = LocationAlwaysPermissionDelegate(callback)
            manager.delegate = delegate
            manager.requestAlwaysAuthorization()
        }

    override fun refreshStatus(): PermissionStatus {
        return getPermissionStatus()
    }
}

private class LocationAlwaysPermissionDelegate(
    val callback: (Boolean) -> Unit
) : NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        val status = CLLocationManager.authorizationStatus()
        callback(
            status == kCLAuthorizationStatusAuthorizedAlways
        )
    }
}
