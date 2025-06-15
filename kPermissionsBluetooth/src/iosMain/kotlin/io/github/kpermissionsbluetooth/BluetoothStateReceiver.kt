package io.github.kpermissionsbluetooth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.darwin.NSObject

actual object BluetoothStateReceiver {
    private val centralManagerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            _isBluetoothOn.value = central.state == CBManagerStatePoweredOn
        }
    }

    private var centralManager: CBCentralManager? = null

    private val _isBluetoothOn = MutableStateFlow(false)
    actual val isBluetoothOn: StateFlow<Boolean> get() = _isBluetoothOn

    actual fun register() {
        centralManager = CBCentralManager(centralManagerDelegate, null)
        _isBluetoothOn.value = centralManager?.state == CBManagerStatePoweredOn
    }

    actual fun unregister() {
        centralManager?.delegate = null
        centralManager = null
    }
}
