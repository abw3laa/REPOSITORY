package com.abw3laa.callrecorder.permissions

data class PermissionState(
    val phoneStateGranted: Boolean,
    val recordAudioGranted: Boolean,
    val notificationsGranted: Boolean
) {
    val readyForCallDetection: Boolean
        get() = phoneStateGranted

    val readyForLocalRecording: Boolean
        get() = phoneStateGranted && recordAudioGranted
}
