package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission

actual fun Permission.refreshStatusCMP() {
    this.getPermissionStatus()
}