package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus
import io.github.kPermissions_api.refreshState

actual suspend fun Permission.refreshStatusCMP(): PermissionStatus {
   return this.refreshState()
}