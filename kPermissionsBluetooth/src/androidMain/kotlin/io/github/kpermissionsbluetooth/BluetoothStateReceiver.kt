package io.github.kpermissionsbluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BluetoothStateReceiver {

    private val _isBluetoothOn = MutableStateFlow(checkBluetoothState())
    val isBluetoothOn: StateFlow<Boolean> get() = _isBluetoothOn

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                _isBluetoothOn.value = state == BluetoothAdapter.STATE_ON
            }
        }
    }

    fun register() {
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(receiver, filter)
        _isBluetoothOn.value = checkBluetoothState()
    }

    fun unregister() {
        context.unregisterReceiver(receiver)
    }

    private fun checkBluetoothState(): Boolean {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager
        return bluetoothManager?.adapter?.isEnabled == true
    }

    private val context: Context
        get() = AppContextProvider.appContext
}