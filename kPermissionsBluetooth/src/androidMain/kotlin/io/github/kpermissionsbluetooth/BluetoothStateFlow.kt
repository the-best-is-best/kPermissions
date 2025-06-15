package io.github.kpermissionsbluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual fun bluetoothStateFlow(): Flow<Boolean> = callbackFlow {
    val context = AppContextProvider.appContext

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                trySend(state == BluetoothAdapter.STATE_ON).isSuccess
            }
        }
    }

    val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
    context.registerReceiver(receiver, filter)

    val initialState = BluetoothAdapter.getDefaultAdapter()?.isEnabled == true
    trySend(initialState).isSuccess

    awaitClose {
        context.unregisterReceiver(receiver)
    }
}
