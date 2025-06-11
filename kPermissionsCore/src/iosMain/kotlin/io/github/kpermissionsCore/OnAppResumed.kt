package io.github.kpermissionsCore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState


@Composable
fun OnAppResumed(onResume: () -> Unit) {
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(Unit) {
        val observer = platform.Foundation.NSNotificationCenter.defaultCenter.addObserverForName(
            name = platform.UIKit.UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null
        ) { _ ->
            currentOnResume()
        }

        onDispose {
            platform.Foundation.NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}