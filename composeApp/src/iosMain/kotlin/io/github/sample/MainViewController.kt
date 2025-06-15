package io.github.sample

import androidx.compose.ui.window.ComposeUIViewController
import io.github.kPermissionsGallery.GalleryPermission

fun MainViewController() = ComposeUIViewController {
    GalleryPermission.setMainAndMaxSdk(
        minSdk = 16,
        maxSdk = 30
    )
    App()
}