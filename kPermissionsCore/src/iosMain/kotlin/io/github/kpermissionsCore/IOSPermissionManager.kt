package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlin.experimental.ExperimentalObjCName

internal fun getStatus(permission: Permission): PermissionStatus =
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
@ObjCName("requestMultiplePermissionsWithStatus")
fun requestMultiplePermissionsWithStatus(
    permissions: List<Permission>,
    onRequest: (Permission, (Boolean) -> Unit) -> Unit,
    onResult: (Boolean) -> Unit
) {
    requestPermissionsSequentially(permissions, 0, true, onRequest, onResult)
}
