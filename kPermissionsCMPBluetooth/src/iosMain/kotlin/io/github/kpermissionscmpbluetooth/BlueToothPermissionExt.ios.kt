package io.github.kpermissionscmpbluetooth

import io.github.kpermissionsbluetooth.BluetoothPermission

actual fun BluetoothPermission.openBluetoothSettingsCMP() {
    throw UnsupportedOperationException("Bluetooth settings cannot be opened directly in IOS.")
}