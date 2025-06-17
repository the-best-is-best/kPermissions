package io.github.kPermissionsAudio

import io.github.kPermissions_api.Permission

expect object ReadAudioPermission : Permission {
    override val name: String
    override val minSdk: Int?
    override val maxSdk: Int?
    override suspend fun isServiceAvailable(): Boolean
    override fun setMainAndMaxSdk(minSdk: Int?, maxSdk: Int?)
}
