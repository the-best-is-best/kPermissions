package io.github.sample

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionState


data class PermissionItem(
    val name: String,
    val permission: Permission,
    val state: PermissionState,
    val onRequest: () -> Unit,
)
