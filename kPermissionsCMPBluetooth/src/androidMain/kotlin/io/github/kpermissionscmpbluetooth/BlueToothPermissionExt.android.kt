package io.github.kpermissionscmpbluetooth

import io.github.kpermissionsbluetooth.BluetoothPermission

actual fun BluetoothPermission.openBluetoothSettingsCMP() {
    this.openAppBluetoothSettings()
}