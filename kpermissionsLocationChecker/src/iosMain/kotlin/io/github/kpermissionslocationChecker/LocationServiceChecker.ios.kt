package io.github.kpermissionslocationChecker

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

actual val locationServiceEnabledFlow: Flow<Boolean> = callbackFlow {
    trySend(CLLocationManager.locationServicesEnabled())

    val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            trySend(CLLocationManager.locationServicesEnabled())
        }
    }

    val locationManager = CLLocationManager()
    locationManager.delegate = delegate

    awaitClose {
        locationManager.delegate = null
    }
}
