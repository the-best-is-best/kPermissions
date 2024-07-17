package io.github.kpermissions.handler.permissions

import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined


fun requestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
    val locationManager = CLLocationManager()
    val status = CLLocationManager.authorizationStatus()
    when (status) {
        kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> onPermissionResult(
            true
        )

        kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
        else -> onPermissionResult(false)
    }
}

fun requestLocationAlwaysPermission(onPermissionResult: (Boolean) -> Unit) {
    val locationManager = CLLocationManager()
    val status = CLLocationManager.authorizationStatus()
    when (status) {
        kCLAuthorizationStatusAuthorizedAlways -> onPermissionResult(true)
        kCLAuthorizationStatusNotDetermined -> locationManager.requestAlwaysAuthorization()
        else -> onPermissionResult(false)
    }
}

fun requestLocationWhenInUsePermission(onPermissionResult: (Boolean) -> Unit) {
    val locationManager = CLLocationManager()
    val status = CLLocationManager.authorizationStatus()
    when (status) {
        kCLAuthorizationStatusAuthorizedWhenInUse -> onPermissionResult(true)
        kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
        else -> onPermissionResult(false)
    }
}