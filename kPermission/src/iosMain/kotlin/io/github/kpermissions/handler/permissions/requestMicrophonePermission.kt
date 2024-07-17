package io.github.kpermissions.handler.permissions

import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVAudioSessionRecordPermissionUndetermined

fun requestMicrophonePermission(onPermissionResult: (Boolean) -> Unit) {
    val permissionStatus = AVAudioSession.sharedInstance().recordPermission()
    when (permissionStatus) {
        AVAudioSessionRecordPermissionGranted -> {
            onPermissionResult(true)
        }

        AVAudioSessionRecordPermissionUndetermined -> {
            AVAudioSession.sharedInstance().requestRecordPermission { granted ->
                onPermissionResult(granted)
            }
        }

        else -> {
            onPermissionResult(false)
        }
    }
}