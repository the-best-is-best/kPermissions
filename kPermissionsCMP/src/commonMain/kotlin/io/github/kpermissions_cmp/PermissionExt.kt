package io.github.kpermissions_cmp

import io.github.kPermissions_api.Permission

enum class PlatformIgnore {
    None,
    IOS,
    Android,

}

private val ignoreMap = mutableMapOf<Permission, PlatformIgnore>()

fun Permission.setIgnore(value: PlatformIgnore) {
    if (this.getIgnore() != PlatformIgnore.None) {
        throw IllegalStateException("Permission ${this.name}  not allowed to set ignore value")
    }
    ignoreMap[this] = value
}

fun Permission.getIgnore(): PlatformIgnore {
    return ignoreMap[this] ?: PlatformIgnore.None
}



