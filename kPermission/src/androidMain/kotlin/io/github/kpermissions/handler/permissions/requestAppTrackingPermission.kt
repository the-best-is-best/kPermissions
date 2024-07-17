package io.github.kpermissions.handler.permissions

fun requestAppTrackingPermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}