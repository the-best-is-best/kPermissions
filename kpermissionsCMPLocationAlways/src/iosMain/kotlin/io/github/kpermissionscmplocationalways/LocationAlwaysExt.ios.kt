package io.github.kpermissionscmplocationalways

import io.github.kpermissionslocationAlways.LocationAlwaysPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject

actual suspend fun LocationAlwaysPermission.openPrivacySettings() =
    withContext(Dispatchers.Default) {
    if (!CLLocationManager.locationServicesEnabled()) {
        val locationManager = CLLocationManager().apply {
            delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    manager.stopUpdatingLocation()
                }

                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    manager.stopUpdatingLocation()
                }
            }
            startUpdatingLocation()
        }
    }
    }

