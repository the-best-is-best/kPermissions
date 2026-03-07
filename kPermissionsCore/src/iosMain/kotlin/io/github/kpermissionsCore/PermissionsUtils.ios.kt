package io.github.kpermissionsCore

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalObjCName
val IOSPermissionManager.currentIosVersion: Int
    get() = UIDevice.currentDevice.systemVersion
        .split(".")
        .firstOrNull()
        ?.toIntOrNull() ?: 0



@OptIn(ExperimentalObjCName::class)
actual fun openAppSettingsPlatform() {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
    if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(
            url,
            options = emptyMap<Any?, Any?>(),
            completionHandler = { success ->
                if (!success) {
                    println("Failed to open URL: $url")
                }
            }
        )

    }
}
