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
    permissions: List<Permission>
): List<PermissionStatus> {
    return permissions.map { permission ->
        when (val status = getStatus(permission)) {
            is PermissionStatus.Granted,
            is PermissionStatus.DeniedPermanently,
            is PermissionStatus.Unavailable,
            is PermissionStatus.NotDeclared -> status

            is PermissionStatus.Denied -> {

                val granted = permission.permissionRequestSuspend()
                if (granted) PermissionStatus.Granted else getStatus(permission)
            }
        }
    }
}
