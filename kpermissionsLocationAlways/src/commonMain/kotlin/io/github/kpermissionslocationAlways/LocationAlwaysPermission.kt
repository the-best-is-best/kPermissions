package io.github.kpermissionslocationAlways

import io.github.kPermissions_api.Permission

expect object LocationAlwaysPermission : Permission {
    override val name: String
    override val minSdk: Int?
    override val maxSdk: Int?

    /**
     * Checks if the location service is available.
     * @return true if the service is available, false otherwise.
     */
    override suspend fun isServiceAvailable(): Boolean

    /**
     * Sets the minimum and maximum SDK versions for this permission.
     * @param minSdk The minimum SDK version.
     * @param maxSdk The maximum SDK version.
     */
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)


}