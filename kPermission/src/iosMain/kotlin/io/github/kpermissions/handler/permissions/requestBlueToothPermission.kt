package io.github.kpermissions.handler.permissions

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.darwin.NSObject

fun requestBlueToothPermission(onPermissionResult: (Boolean) -> Unit) {
    var centralManager: CBCentralManager? = null
    centralManager =
        CBCentralManager(delegate = object : NSObject(), CBCentralManagerDelegateProtocol {
            override fun centralManagerDidUpdateState(central: CBCentralManager) {
                when (central.state) {
                    CBCentralManagerStatePoweredOn -> {

                        onPermissionResult(true) // Bluetooth is powered on
                    }

                    else -> {
                        onPermissionResult(false) // Bluetooth is either off or in an unknown state
                    }
                }
                println("central state ${central.state.toInt()}")

                centralManager?.delegate = null // Remove delegate to avoid memory leaks
                centralManager = null
            }
        }, queue = null)

}

