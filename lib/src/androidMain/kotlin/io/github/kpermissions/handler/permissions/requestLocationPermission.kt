package io.github.kpermissions.handler.permissions

import android.Manifest
import android.os.Build
import io.github.kpermissions.handler.permissionRequest


fun requestLocationPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = Manifest.permission.ACCESS_FINE_LOCATION

    permissionRequest(arrayOf(permission), onPermissionResult)

}


fun requestLocationAlwaysPermission(onPermissionResult: (Boolean) -> Unit) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    } else {
        null
    }

    if (permission != null) {
        permissionRequest(arrayOf(permission), onPermissionResult)
    } else {
        onPermissionResult(true)
    }
}


fun requestLocationWhenInUsePermission(onPermissionResult: (Boolean) -> Unit) {
    val permission1 = Manifest.permission.ACCESS_FINE_LOCATION
    val permission2 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Manifest.permission.ACCESS_BACKGROUND_LOCATION

    } else {
        null
    }
    if (permission2 != null) {
        permissionRequest(arrayOf(permission1, permission2), onPermissionResult)
    } else {
        permissionRequest(arrayOf(permission1), onPermissionResult)
    }


}




