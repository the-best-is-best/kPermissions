package io.github.kpermissionslocationAlways

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

    override suspend fun isServiceAvailable(): Boolean {
        return withContext(Dispatchers.Default) {
            CLLocationManager.locationServicesEnabled()
        }

    }


    override fun getPermissionStatus(): PermissionStatus {
        println("status always permission: ${CLLocationManager.authorizationStatus()}")
        println("kCLAuthorizationStatusAuthorizedAlways: $kCLAuthorizationStatusAuthorizedAlways")
        val status = when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusAuthorizedAlways -> PermissionStatus.Granted
            kCLAuthorizationStatusDenied -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusRestricted -> PermissionStatus.DeniedPermanently
            kCLAuthorizationStatusNotDetermined -> PermissionStatus.Denied
            else -> PermissionStatus.Denied
        }
        return status
    }

    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            val manager = CLLocationManager()
            val delegate = LocationAlwaysPermissionDelegate(callback)
            manager.delegate = delegate
            manager.requestAlwaysAuthorization()
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
