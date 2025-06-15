package io.github.kpermissionslocationChecker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual val locationServiceEnabledFlow: Flow<Boolean>
    get() = callbackFlow {
        val context = AppContextProvider.appContext
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isFineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val isCoarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isFineGranted && !isCoarseGranted) {
            trySend(false)
            close()
            return@callbackFlow
        }

        val listener = object : LocationListener {
            override fun onProviderEnabled(provider: String) {
                trySend(
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                )
            }

            override fun onProviderDisabled(provider: String) {
                trySend(
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                )
            }

            override fun onLocationChanged(location: Location) {}
        }

        trySend(
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        )

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, listener)

        awaitClose {
            locationManager.removeUpdates(listener)
        }
    }
