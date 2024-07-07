package io.github.kpermissions.handler

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

fun openAppSettings() {
    val url = NSURL(string = "App-Prefs:root=General")
    if (UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    }
}
