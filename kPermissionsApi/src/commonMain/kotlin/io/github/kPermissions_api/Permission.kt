package io.github.kPermissions_api

expect interface Permission {
    val name: String
    val minSdk: Int?
    val maxSdk: Int?
    fun isServiceAvailable(): Boolean
    suspend fun refreshStatus(): PermissionStatus
    fun setMainAndMaxSdk(minSdk: Int? = null, maxSdk: Int? = null)
}