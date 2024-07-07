package io.github.kpermissions.handler

import io.github.kpermissions.enum.EnumAppPermission

interface PermissionCallback {

    fun requestPermission(permission: EnumAppPermission, onPermissionResult: (Boolean) -> Unit)
}

expect class PermissionHandler() : PermissionCallback {
    companion object {
        internal var openSetting: Boolean
    }
}

