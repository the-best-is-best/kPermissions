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
    val status = getStatus(permission)

    when (status) {
        is PermissionStatus.Granted -> {
            onResult(true)
        }

        is PermissionStatus.Unavailable,
        is PermissionStatus.NotDeclared -> {
            onResult(false)
        }

        else -> {
            permissionRequest { granted ->
                onResult(granted)
            }
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
    when (val current = getStatus(permission)) {
        is PermissionStatus.Granted,
        is PermissionStatus.Unavailable,
        is PermissionStatus.NotDeclared -> {
            onResult(current)
        }

        else -> {
            permissionRequest { granted ->
                if (granted) {
                    onResult(PermissionStatus.Granted)
                } else {
                    // إعادة التحقق بعد الرفض
                    onResult(getStatus(permission))
                }
            }
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
        val status = getStatus(permission)

        when (status) {
            is PermissionStatus.Granted -> {
                remaining--
                if (remaining == 0) onResult(allGranted)
            }

            is PermissionStatus.Unavailable,
            is PermissionStatus.NotDeclared -> {
                allGranted = false
                remaining--
                if (remaining == 0) onResult(allGranted)
            }

            else -> {
                onRequest(permission) { granted ->
                    if (!granted) allGranted = false
                    remaining--
                    if (remaining == 0) onResult(allGranted)
                }
            }
        }
    }
}
