package io.github.kpermissions.handler.permissions

import android.Manifest
import io.github.kpermissions.handler.permissionRequest

fun requestBluetoothPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.BLUETOOTH

    permissionRequest(arrayOf(permission), onPermissionResult)


}