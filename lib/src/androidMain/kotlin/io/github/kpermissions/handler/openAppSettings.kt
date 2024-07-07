package io.github.kpermissions.handler

import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun openAppSettings() {

    val i = Intent()
    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    i.addCategory(Intent.CATEGORY_DEFAULT)
    i.setData(Uri.parse("package:" + PermissionHandler.getAppContext().packageName))
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    PermissionHandler.getAppContext().startActivity(i)
}