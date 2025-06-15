package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus

internal fun requestPermissionsSequentially(
    permissions: List<Permission>,
    index: Int = 0,
    grantedSoFar: Boolean = true,
    onComplete: (Boolean) -> Unit
) {
    if (index >= permissions.size) {
        onComplete(grantedSoFar)
        return
    }

    val permission = permissions[index]
    when (val status = getStatus(permission)) {
        is PermissionStatus.Granted,
        is PermissionStatus.DeniedPermanently -> {
            requestPermissionsSequentially(
                permissions,
                index + 1,
                grantedSoFar && status is PermissionStatus.Granted,
                onComplete
            )
        }

        is PermissionStatus.Unavailable,
        is PermissionStatus.NotDeclared -> {
            requestPermissionsSequentially(
                permissions,
                index + 1,
                false,
                onComplete
            )
        }

        else -> {
            permission.permissionRequest { granted ->
                val updatedGranted = grantedSoFar && granted
                requestPermissionsSequentially(
                    permissions,
                    index + 1,
                    updatedGranted,
                    onComplete
                )
            }
        }
    }
}


internal fun requestPermissionsSequentially(
    permissions: List<Permission>,
    index: Int = 0,
    grantedSoFar: Boolean = true,
    onRequest: (Permission, (Boolean) -> Unit) -> Unit,
    onComplete: (Boolean) -> Unit
) {
    if (index >= permissions.size) {
        onComplete(grantedSoFar)
        return
    }

    val permission = permissions[index]
    when (val status = getStatus(permission)) {
        is PermissionStatus.Granted,
        is PermissionStatus.DeniedPermanently -> {
            requestPermissionsSequentially(
                permissions,
                index + 1,
                grantedSoFar && status is PermissionStatus.Granted,
                onRequest,
                onComplete
            )
        }

        is PermissionStatus.Unavailable,
        is PermissionStatus.NotDeclared -> {
            requestPermissionsSequentially(
                permissions,
                index + 1,
                false,
                onRequest,
                onComplete
            )
        }

        else -> {
            onRequest(permission) { granted ->
                requestPermissionsSequentially(
                    permissions,
                    index + 1,
                    grantedSoFar && granted,
                    onRequest,
                    onComplete
                )
            }
        }
    }
}
