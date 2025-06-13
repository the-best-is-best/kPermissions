package io.github.kPermissions_api

actual interface Permission {
    actual val name: String
    val androidPermissionName: String?
    actual var minSdk: Int?
    actual var maxSdk: Int?
}