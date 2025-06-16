package io.github.kpermissionscmplocationalways

import io.github.kpermissionslocationAlways.LocationAlwaysPermission

actual fun LocationAlwaysPermission.openPrivacySettings() {
    throw UnsupportedOperationException("Opening privacy settings is not supported on IOS.")

}