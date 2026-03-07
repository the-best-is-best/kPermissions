package io.github.kpermissions_cmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidBecomeActiveNotification


@Composable
internal fun OnAppResumed(onResume: () -> Unit) {
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = null
        ) { _ ->
            currentOnResume()
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }
}