package io.github.kpermissionsbluetooth

import io.github.kPermissions_api.Permission

expect object BluetoothPermission : Permission {
    override val name: String
    override val minSdk: Int?
    override val maxSdk: Int?
    override suspend fun isServiceAvailable(): Boolean
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
}