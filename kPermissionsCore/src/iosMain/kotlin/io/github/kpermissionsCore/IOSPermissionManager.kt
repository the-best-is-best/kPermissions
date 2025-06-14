package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlin.experimental.ExperimentalObjCName


private fun getStatus(permission: Permission): PermissionStatus =
    permission.getPermissionStatus()

@OptIn(ExperimentalObjCName::class)
@ObjCName("requestPermission")
fun requestPermission(
    permission: Permission,
    permissionRequest: ((Boolean) -> Unit) -> Unit,
    onResult: (Boolean) -> Unit
) {
    if (!permission.isServiceAvailable()) {
        onResult(false)
        return
    }
    val currentStatus = getStatus(permission)
    if (currentStatus == PermissionStatus.Granted) {
        onResult(true)
    } else {
        permissionRequest { granted ->
            onResult(granted)
        }
    }
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("currentStatus")
fun currentStatus(permission: Permission): PermissionStatus =
    getStatus(permission)

@OptIn(ExperimentalObjCName::class)
@ObjCName("requestPermissionWithStatus")
fun requestPermissionWithStatus(
    permission: Permission,
    permissionRequest: ((Boolean) -> Unit) -> Unit,
    onResult: (PermissionStatus) -> Unit
) {
    if (!permission.isServiceAvailable()) {
        onResult(PermissionStatus.Unavailable)
        return
    }
    val currentStatus = getStatus(permission)
    if (currentStatus == PermissionStatus.Granted) {
        onResult(PermissionStatus.Granted)
    } else {
        permissionRequest { granted ->
            onResult(if (granted) PermissionStatus.Granted else getStatus(permission))
        }
    }
}

@OptIn(ExperimentalObjCName::class)
@ObjCName("requestMultiplePermissionsWithStatus")
fun requestMultiplePermissionsWithStatus(
    permissions: List<Permission>,
    onRequest: (Permission, (Boolean) -> Unit) -> Unit,
    onResult: (Boolean) -> Unit
) {
    var allGranted = true
    var remaining = permissions.size

    if (remaining == 0) {
        onResult(true)
        return
    }

    permissions.forEach { permission ->
        if (!permission.isServiceAvailable()) {
            allGranted = false
            remaining--
            if (remaining == 0) onResult(allGranted)
            return@forEach
        }
        val currentStatus = getStatus(permission)

        if (currentStatus == PermissionStatus.Granted) {
            remaining--
            if (remaining == 0) onResult(allGranted)
        } else {
            onRequest(permission) { granted ->
                if (!granted) allGranted = false
                remaining--
                if (remaining == 0) onResult(allGranted)
            }
        }
    }
}
