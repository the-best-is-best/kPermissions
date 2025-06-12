package io.github.kpermissionsCamera


actual fun cameraPermissionRequest(): ((Boolean) -> Unit) -> Unit {
    return {}
}

actual fun registerIosProvider() {
}