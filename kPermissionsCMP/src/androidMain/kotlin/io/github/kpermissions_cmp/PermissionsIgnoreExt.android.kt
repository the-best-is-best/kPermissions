package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.checkPermissionStatus

actual fun Permission.checkPermissionStatusCMP(): PermissionStatus {
    return this.checkPermissionStatus()
}