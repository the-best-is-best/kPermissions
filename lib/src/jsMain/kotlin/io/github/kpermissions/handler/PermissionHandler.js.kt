package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission

actual class PermissionHandler actual constructor() {
    actual fun requestPermission(
        permission: EnumAppPermission,
        onPermissionResult: (Boolean) -> Unit
    ) {
        onPermissionResult(true)
    }

    actual companion object {
        internal actual var openSetting: Boolean = false
    }
}