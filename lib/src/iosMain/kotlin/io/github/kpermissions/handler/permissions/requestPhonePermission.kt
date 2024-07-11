package io.github.kpermissions.handler.permissions

fun requestPhonePermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}

