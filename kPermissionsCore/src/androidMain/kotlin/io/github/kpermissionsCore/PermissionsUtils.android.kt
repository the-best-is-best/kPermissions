package io.github.kpermissionsCore

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.startup.Initializer

internal object AppContextProvider {
    lateinit var appContext: Context

}

internal class ApplicationContextInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        AppContextProvider.appContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}

actual fun openAppSettingsPlatform() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", AppContextProvider.appContext.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AppContextProvider.appContext.startActivity(intent)
}