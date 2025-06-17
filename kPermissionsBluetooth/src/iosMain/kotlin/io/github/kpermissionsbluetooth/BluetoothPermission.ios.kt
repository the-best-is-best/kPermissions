package io.github.kpermissionsbluetooth

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManager
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerAuthorizationDenied
import platform.CoreBluetooth.CBManagerAuthorizationNotDetermined
import platform.CoreBluetooth.CBManagerAuthorizationRestricted
import platform.CoreBluetooth.CBManagerStatePoweredOff
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateResetting
import platform.CoreBluetooth.CBManagerStateUnauthorized
import platform.CoreBluetooth.CBManagerStateUnknown
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
actual object BluetoothPermission : Permission {
    actual override val name: String get() = "bluetooth"

    private var _minSdk: Int? = null
    private var _maxSdk: Int? = null
    actual override val minSdk: Int? get() = _minSdk
    actual override val maxSdk: Int? get() = _maxSdk

    actual override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?) {
        _minSdk = minSdk
        _maxSdk = maxSdk
    }

    // ============ Service Availability ============
    actual override suspend fun isServiceAvailable(): Boolean {
        return CBCentralManager().state == CBManagerStatePoweredOn
    }

    // ============ Status ============
    override suspend fun getPermissionStatus(): PermissionStatus {
        return getBluetoothStatus()
    }


    private fun getBluetoothStatus(): PermissionStatus {
        val versionCValue: CValue<NSOperatingSystemVersion> =
            NSProcessInfo.processInfo.operatingSystemVersion
        val (major, minor, patch) = versionCValue.useContents {
            Triple(majorVersion, minorVersion, patchVersion)
        }
        // iOS 13+ API to get bluetooth authorization status
        return if (major >= 13) {
            when (CBManager.authorization) {
                CBManagerAuthorizationAllowedAlways -> PermissionStatus.Granted
                CBManagerAuthorizationNotDetermined -> PermissionStatus.Denied
                CBManagerAuthorizationDenied -> PermissionStatus.DeniedPermanently
                CBManagerAuthorizationRestricted -> PermissionStatus.DeniedPermanently
                else -> PermissionStatus.DeniedPermanently
            }
        } else {
            // For older iOS versions, fallback to checking CBCentralManager state
            val centralManager = CBCentralManager(
                null,
                null
            ) // delegate null (may cause warning but ok for state check)
            when (centralManager.state) {
                CBManagerStatePoweredOn -> PermissionStatus.Granted
                CBManagerStateUnauthorized -> PermissionStatus.DeniedPermanently
                CBManagerStateUnknown, CBManagerStatePoweredOff, CBManagerStateResetting -> PermissionStatus.Denied
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
