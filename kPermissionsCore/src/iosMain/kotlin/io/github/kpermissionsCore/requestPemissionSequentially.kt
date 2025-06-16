package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private suspend fun Permission.permissionRequestSuspend(): Boolean =
    suspendCoroutine { continuation ->
        this.permissionRequest { granted ->
            continuation.resume(granted)
        }
    }

internal suspend fun requestPermissionsSequentially(
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
            // ✅ Use suspend version of permissionRequest
            val granted = permission.permissionRequestSuspend()
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

internal suspend fun requestPermissionsSequentially(
    permissions: List<Permission>,
    index: Int = 0,
    grantedSoFar: Boolean = true,
    onRequest: suspend (Permission) -> Boolean,
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
            val granted = onRequest(permission)
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
