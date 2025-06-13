package io.github.kPermissions_api

actual interface Permission {
    actual val name: String
    val type: PermissionType
}