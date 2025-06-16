package io.github.kpermissionlocationwheninuseext

import io.github.kpermissionslocationWhenInUse.LocationInUsePermission
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject

actual suspend fun LocationInUsePermission.openPrivacySettings() {
    if (!CLLocationManager.locationServicesEnabled()) {
        val locationManager = CLLocationManager()
        locationManager.delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                manager.stopUpdatingLocation()
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                manager.stopUpdatingLocation()
            }
        }
        locationManager.startUpdatingLocation()
    }
}
