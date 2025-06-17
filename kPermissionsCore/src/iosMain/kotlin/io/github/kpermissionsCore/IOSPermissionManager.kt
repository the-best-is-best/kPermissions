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

    ): PermissionStatus {

    val status = getStatus(permission)

    when (status) {
        is PermissionStatus.Granted -> {
            return PermissionStatus.Granted
        }

        is PermissionStatus.Unavailable -> {
            return PermissionStatus.Unavailable
        }
        is PermissionStatus.NotDeclared -> {
            return PermissionStatus.NotDeclared
        }

        PermissionStatus.Denied -> {
            val granted = suspendCoroutine { continuation ->
                permission.permissionRequest { granted ->
                    continuation.resume(granted)
                }
            }

            return if (granted) {
                PermissionStatus.Granted
            } else {
                getStatus(permission) // 👈 هنا تقدر تستدعيها برا callback لأنها في Coroutine دلوقتي
            }
        }

        PermissionStatus.DeniedPermanently -> {
            return PermissionStatus.DeniedPermanently
        }
    }
}


@OptIn(ExperimentalObjCName::class)
@ObjCName("requestMultiplePermissions")
suspend fun requestMultiplePermissions(
    permissions: List<Permission>,

    ): List<PermissionStatus> {
    return requestPermissionsSequentially(
        permissions = permissions,
    )
}
