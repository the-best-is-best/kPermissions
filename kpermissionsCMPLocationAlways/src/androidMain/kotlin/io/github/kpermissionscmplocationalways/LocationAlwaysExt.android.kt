package io.github.kpermissionscmplocationalways

import io.github.kpermissionslocationAlways.LocationAlwaysPermission

actual suspend fun LocationAlwaysPermission.openPrivacySettings() {
    this.openServiceSettings()
}