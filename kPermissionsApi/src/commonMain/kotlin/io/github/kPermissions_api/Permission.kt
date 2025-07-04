package io.github.kPermissions_api

expect interface Permission {
    val name: String
    val minSdk: Int?
    val maxSdk: Int?
    suspend fun isServiceAvailable(): Boolean
    fun setMainAndMaxSdk(minSdk: Int? = null, maxSdk: Int? = null)
}