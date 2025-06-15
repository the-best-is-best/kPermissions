package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus

internal fun requestPermissionsSequentially(
    permissions: List<Permission>,
    index: Int = 0,
    onComplete: (Boolean) -> Unit
) {
    if (index >= permissions.size) {
        onComplete(true)
        return
    }

    val permission = permissions[index]
    val status = getStatus(permission)

    when (status) {
        is PermissionStatus.Granted -> {
            requestPermissionsSequentially(permissions, index + 1, onComplete)
        }

        is PermissionStatus.Unavailable,
        is PermissionStatus.NotDeclared -> {
            onComplete(false)
        }

        else -> {
            permission.permissionRequest { granted ->
                if (!granted) {
                    onComplete(false)
                } else {
                    requestPermissionsSequentially(permissions, index + 1, onComplete)
                }
            }
        }
    }
}

internal fun requestPermissionsSequentially(
    permissions: List<Permission>,
    index: Int = 0,
    onRequest: (Permission, (Boolean) -> Unit) -> Unit,
    onComplete: (Boolean) -> Unit
) {
    if (index >= permissions.size) {
        onComplete(true)
        return
    }

    val permission = permissions[index]
    val status = getStatus(permission)

    when (status) {
        is PermissionStatus.Granted -> requestPermissionsSequentially(
            permissions,
            index + 1,
            onRequest,
            onComplete
        )

        is PermissionStatus.Unavailable, is PermissionStatus.NotDeclared -> onComplete(false)
        else -> {
            onRequest(permission) { granted ->
                if (!granted) {
                    onComplete(false)
                } else {
                    requestPermissionsSequentially(permissions, index + 1, onRequest, onComplete)
                }
            }
        }
    }
}

