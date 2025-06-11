package io.github.kpermissionsCore

import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(name = "IOSPermissionManager", exact = true)
class IOSPermissionManager(
    private val permission: Permission,
    private val permissionRequest: ((Boolean) -> Unit) -> Unit,
) {
    private val getStatus: () -> PermissionStatus =
        { PermissionStatusRegistry.getStatus(permission.name) }

    @ObjCName("requestPermission")
    fun requestPermission(onResult: (Boolean) -> Unit) {
        val currentStatus = getStatus()
        if (currentStatus == PermissionStatus.Granted) {
            onResult(true)
        } else {
            permissionRequest { granted ->
                onResult(granted)
            }
        }
    }

    @ObjCName("currentStatus")
    fun currentStatus(): PermissionStatus = getStatus()
}
