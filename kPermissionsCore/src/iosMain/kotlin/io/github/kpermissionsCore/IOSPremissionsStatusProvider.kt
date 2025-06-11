package io.github.kpermissionsCore

typealias PermissionStatusProvider = () -> PermissionStatus

object PermissionStatusRegistry {
    private val providers = mutableMapOf<String, PermissionStatusProvider>()

    fun register(key: String, provider: PermissionStatusProvider) {
        providers[key] = provider
    }

    fun getStatus(key: String): PermissionStatus {
        return providers[key]?.invoke()
            ?: error("No status provider registered for key: $key")
    }
}
