package io.github.kPermissions_api

expect interface Permission {
    val name: String
    var minSdk: Int?
    var maxSdk: Int?
}