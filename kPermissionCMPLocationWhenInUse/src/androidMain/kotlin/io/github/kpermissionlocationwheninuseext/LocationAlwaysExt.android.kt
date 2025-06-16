package io.github.kpermissionlocationwheninuseext

import io.github.kpermissionslocationWhenInUse.LocationInUsePermission

actual suspend fun LocationInUsePermission.openPrivacySettings() {
    this.openServiceSettings()
}