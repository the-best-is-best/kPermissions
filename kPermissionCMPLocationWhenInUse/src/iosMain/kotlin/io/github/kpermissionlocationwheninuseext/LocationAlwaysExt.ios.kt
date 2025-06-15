package io.github.kpermissionlocationwheninuseext

import io.github.kpermissionslocationWhenInUse.LocationInUsePermission

actual fun LocationInUsePermission.openPrivacySettings() {
    throw UnsupportedOperationException("Opening privacy settings is not supported on IOS.")
}