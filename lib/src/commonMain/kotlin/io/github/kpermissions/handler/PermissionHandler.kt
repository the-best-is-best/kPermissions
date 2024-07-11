package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission


expect class PermissionHandler() {
    fun requestPermission(permission: EnumAppPermission, onPermissionResult: (Boolean) -> Unit)

    companion object {
        internal var openSetting: Boolean
    }
}

