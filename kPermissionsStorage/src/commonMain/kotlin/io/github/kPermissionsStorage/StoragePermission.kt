package io.github.kPermissionsStorage

import io.github.kPermissions_api.Permission

expect object WriteStoragePermission : Permission {
    override val name: String
    override val minSdk: Int?
    override val maxSdk: Int?
    override suspend fun isServiceAvailable(): Boolean
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
}

expect object ReadStoragePermission : Permission {
    override val name: String
    override val minSdk: Int?
    override val maxSdk: Int?
    override suspend fun isServiceAvailable(): Boolean
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
}