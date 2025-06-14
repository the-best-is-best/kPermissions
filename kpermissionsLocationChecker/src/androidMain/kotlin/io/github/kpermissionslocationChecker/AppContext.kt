package io.github.kpermissionslocationChecker

import android.content.Context
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

