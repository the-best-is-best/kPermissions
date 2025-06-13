package io.github.kPermissions_api

import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("Permission", exact = true)
actual interface Permission {
    actual val name: String
    val permissionRequest: ((Boolean) -> Unit) -> Unit
    fun getPermissionStatus(): PermissionStatus
}