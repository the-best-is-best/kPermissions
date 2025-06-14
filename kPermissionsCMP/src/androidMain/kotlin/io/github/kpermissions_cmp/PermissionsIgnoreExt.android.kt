package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.refreshStatus

actual fun Permission.refreshStatusCMP(): PermissionStatus {
    return this.refreshStatus()
}