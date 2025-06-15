package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.refreshState

actual fun Permission.refreshStatusCMP() {
    this.refreshState()
}