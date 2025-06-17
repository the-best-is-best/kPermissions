package io.github.kpermissionsbluetooth

import android.Manifest
import android.content.Context
import android.os.Build
import io.github.kPermissions_api.Permission


actual object BluetoothPermission : Permission {

    override val name: String = "bluetooth"

    override val androidPermissionName: String? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            null
        }

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null
    override val minSdk: Int? get() = _minSdk
    override val maxSdk: Int? get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    override suspend fun isServiceAvailable(): Boolean {
        val bluetoothManager =
            AppContextProvider.appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? android.bluetooth.BluetoothManager
        val adapter = bluetoothManager?.adapter
        return adapter?.isEnabled == true
    }

    fun openAppBluetoothSettings() {
        val context = AppContextProvider.appContext
        val intent =
            android.content.Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
        context.startActivity(intent)
    }


}
