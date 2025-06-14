package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission
import io.github.kPermissions_api.PermissionStatus


enum class PlatformIgnore {
    None,
    IOS,
    Android,

}

private val ignoreMap = mutableMapOf<Permission, PlatformIgnore>()

fun Permission.setIgnore(value: PlatformIgnore) {
    ignoreMap[this] = value
}

fun Permission.getIgnore(): PlatformIgnore {
    return ignoreMap[this] ?: PlatformIgnore.None
}


expect fun Permission.refreshStatusCMP(): PermissionStatus