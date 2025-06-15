package io.github.kpermissionsbluetooth

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManager
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerAuthorizationDenied
import platform.CoreBluetooth.CBManagerAuthorizationNotDetermined
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateUnknown
import platform.Foundation.NSSelectorFromString
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual object BluetoothPermission : Permission {

    override val name: String get() = "bluetooth"

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null
    override val minSdk: Int? get() = _minSdk
    override val maxSdk: Int? get() = _maxSdk

    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    // ============ Service Availability ============
    override fun isServiceAvailable(): Boolean {
        return CBCentralManager().state == CBManagerStatePoweredOn
    }

    // ============ Status ============
    override fun getPermissionStatus(): PermissionStatus {
        return getBluetoothStatus()
    }

    override fun checkPermissionStatus(): PermissionStatus {
        return getBluetoothStatus()
    }

    private fun getBluetoothStatus(): PermissionStatus {
        return if (CBManager.resolveClassMethod(NSSelectorFromString("authorization"))) {
            when (CBManager.authorization) {
                CBManagerAuthorizationAllowedAlways -> PermissionStatus.Granted
                CBManagerAuthorizationNotDetermined -> PermissionStatus.Denied
                CBManagerAuthorizationDenied -> PermissionStatus.DeniedPermanently
                else -> PermissionStatus.DeniedPermanently
            }
        } else {
            when (CBCentralManager().state) {
                CBManagerStatePoweredOn -> PermissionStatus.Granted
                CBManagerStateUnknown -> PermissionStatus.Denied
                else -> PermissionStatus.DeniedPermanently
            }
        }
    }

    // ============ Request ============
    override val permissionRequest: ((Boolean) -> Unit) -> Unit
        get() = { callback ->
            CBCentralManager(object : NSObject(), CBCentralManagerDelegateProtocol {
                override fun centralManagerDidUpdateState(central: CBCentralManager) {
                    val granted = central.state == CBManagerStatePoweredOn
                    callback(granted)
                }
            }, null)
        }
}
