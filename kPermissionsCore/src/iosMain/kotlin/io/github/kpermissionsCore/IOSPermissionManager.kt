package io.github.kpermissionsCore

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.ExperimentalObjCName

internal suspend fun getStatus(permission: Permission): PermissionStatus {
    if (!permission.isServiceAvailable() && permission.getPermissionStatus() == PermissionStatus.DeniedPermanently) {
        return PermissionStatus.Unavailable
    }
    if (!permission.isServiceAvailable() && permission.getPermissionStatus() == PermissionStatus.Granted) {
        return PermissionStatus.Unavailable
    }
    return permission.getPermissionStatus()
}

internal val PermissionStatus.canRequest: Boolean
    get() = this == PermissionStatus.Denied || this == PermissionStatus.NotDeclared


@OptIn(ExperimentalObjCName::class)
@ObjCName("requestPermission")
suspend fun requestPermission(
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
suspend fun requestMultiplePermissionsWithStatus(
    permissions: List<Permission>,
    onRequest: (Permission, (Boolean) -> Unit) -> Unit,
    onResult: (Boolean) -> Unit
) {
    requestPermissionsSequentially(
        permissions = permissions,
        onRequest = { permission ->
            suspendCoroutine { continuation ->
                onRequest(permission) { granted ->
                    continuation.resume(granted)
                }
            }
        },
        onComplete = onResult
    )
}
