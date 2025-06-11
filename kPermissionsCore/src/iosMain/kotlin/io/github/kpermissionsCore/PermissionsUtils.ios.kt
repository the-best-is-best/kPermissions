package io.github.kpermissionsCore

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

actual fun openAppSettingsPlatform() {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
    if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}