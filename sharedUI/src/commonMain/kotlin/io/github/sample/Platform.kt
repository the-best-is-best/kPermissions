package io.github.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform