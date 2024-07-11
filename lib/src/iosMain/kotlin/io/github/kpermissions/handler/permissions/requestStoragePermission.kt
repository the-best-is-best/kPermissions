package io.github.kpermissions.handler.permissions

fun requestStoragePermission(onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
}

