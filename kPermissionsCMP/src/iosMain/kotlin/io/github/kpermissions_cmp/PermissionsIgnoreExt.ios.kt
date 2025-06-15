package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus

actual fun Permission.refreshStatusCMP(): PermissionStatus {
    return this.checkPermissionStatus()
}