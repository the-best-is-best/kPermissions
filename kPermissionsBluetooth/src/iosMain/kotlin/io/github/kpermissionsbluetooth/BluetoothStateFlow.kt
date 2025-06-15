package io.github.kpermissionsbluetooth

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.darwin.NSObject

actual fun bluetoothStateFlow(): Flow<Boolean> = callbackFlow {
    val delegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            trySend(central.state == CBManagerStatePoweredOn).isSuccess
        }
    }

    val manager = CBCentralManager(delegate, null)
    trySend(manager.state == CBManagerStatePoweredOn).isSuccess

    awaitClose {
        manager.delegate = null
    }
}
