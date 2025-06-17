package io.github.kpermissionslocationChecker

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.darwin.NSObject
@OptIn(DelicateCoroutinesApi::class)
actual val locationServiceEnabledFlow: Flow<Boolean> = callbackFlow {

	fun sendCurrentStatus() {
		val enabled = CLLocationManager.locationServicesEnabled() &&
				CLLocationManager.authorizationStatus().let {
					it == kCLAuthorizationStatusAuthorizedAlways ||
							it == kCLAuthorizationStatusAuthorizedWhenInUse
				}
		trySend(enabled)
	}

	withContext(Dispatchers.Default) {
		sendCurrentStatus()
	}

	// Delegate لمراقبة التغيير داخل التطبيق
	val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
		override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
			GlobalScope.launch(Dispatchers.Default) {
				sendCurrentStatus()
			}
		}
	}

	val locationManager = CLLocationManager()
	locationManager.delegate = delegate

	val observer = NSNotificationCenter.defaultCenter.addObserverForName(
		name = UIApplicationDidBecomeActiveNotification,
		`object` = null,
		queue = null
	) {
		// لما يرجع من الإعدادات، أعد إرسال الحالة
		GlobalScope.launch(Dispatchers.Default) {
			sendCurrentStatus()
		}
	}

	awaitClose {
		locationManager.delegate = null
		NSNotificationCenter.defaultCenter.removeObserver(observer)
	}
}
