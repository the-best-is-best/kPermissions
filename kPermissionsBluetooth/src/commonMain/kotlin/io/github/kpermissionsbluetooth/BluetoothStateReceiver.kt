package io.github.kpermissionsbluetooth

import kotlinx.coroutines.flow.StateFlow

expect object BluetoothStateReceiver {
    val isBluetoothOn: StateFlow<Boolean>
    fun register()
    fun unregister()
}