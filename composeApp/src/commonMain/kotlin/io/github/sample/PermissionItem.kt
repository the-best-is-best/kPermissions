package io.github.sample

import io.github.kpermissionsCore.Permission
import io.github.kpermissionsCore.PermissionState

data class PermissionItem(
    val name: String,
    val permission: Permission,
    val state: PermissionState,
    val onRequest: () -> Unit,
)
