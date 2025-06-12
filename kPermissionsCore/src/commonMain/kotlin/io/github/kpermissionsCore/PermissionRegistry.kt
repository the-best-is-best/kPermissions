package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

typealias PermissionProvider = @Composable (Permission, (Boolean) -> Unit) -> PermissionState


object PermissionRegistryInternal {
    val permissionProviders = mutableMapOf<KClass<out Permission>, PermissionProvider>()

    fun registerPermissionProvider(
        permissionClass: KClass<out Permission>,
        provider: PermissionProvider,
    ) {
        permissionProviders[permissionClass] = provider
    }
}


@Composable
internal fun permissionStateHolder(
    permission: Permission,
    onPermissionResult: (Boolean) -> Unit,
): PermissionState {
    val provider = PermissionRegistryInternal.permissionProviders[permission::class]
        ?: error("No PermissionProvider registered for ${permission::class.simpleName}")
    return provider(permission, onPermissionResult)
}




