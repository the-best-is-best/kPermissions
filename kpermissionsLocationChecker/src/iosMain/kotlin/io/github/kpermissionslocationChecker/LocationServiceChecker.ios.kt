package io.github.kpermissionslocationChecker

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.darwin.NSObject

@OptIn(DelicateCoroutinesApi::class)
actual val locationServiceEnabledFlow: Flow<Boolean> = callbackFlow {

	withContext(Dispatchers.Default) {
		trySend(CLLocationManager.locationServicesEnabled())
	}

	val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
		override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
			// شغل على background thread
			kotlinx.coroutines.GlobalScope.launch(Dispatchers.Default) {
				val enabled = CLLocationManager.locationServicesEnabled() &&
						CLLocationManager.authorizationStatus().let {
							it == kCLAuthorizationStatusAuthorizedAlways ||
									it == kCLAuthorizationStatusAuthorizedWhenInUse
						}
				trySend(enabled)
			}
		}
	}


	val locationManager = CLLocationManager()
	locationManager.delegate = delegate

	awaitClose {
		locationManager.delegate = null
	}
}
