package io.github.kPermissions_api

import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("Permission", exact = true)
actual interface Permission {
    actual val name: String
    val permissionRequest: ((Boolean) -> Unit) -> Unit
    actual suspend fun isServiceAvailable(): Boolean
    suspend fun getPermissionStatus(): PermissionStatus
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)

}