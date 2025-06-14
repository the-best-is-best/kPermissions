package io.github.kPermissions_api

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual val minSdk: Int?
    actual val maxSdk: Int?
    actual fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
    actual fun isServiceAvailable(): Boolean
}