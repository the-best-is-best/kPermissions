package io.github.kpermissionlocationwheninuseext

import io.github.kpermissionslocationWhenInUse.LocationInUsePermission

actual fun LocationInUsePermission.openPrivacySettings() {
    this.openServiceSettings()
}