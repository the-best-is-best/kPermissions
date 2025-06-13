package io.github.kPermissions_api

enum class PermissionType {
    Gallery,
    Camera,
    WriteStorage,
    ReadStorage,
    ReadAudio,
    ReadVideo

}

expect interface Permission {
    val name: String
}