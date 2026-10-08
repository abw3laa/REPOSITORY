package com.abw3laa.callrecorder.diagnostics

data class DeviceCapability(
    val apiLevel: Int,
    val manufacturer: String,
    val model: String,
    val phoneStatePermission: Boolean,
    val recordAudioPermission: Boolean,
    val notificationsPermission: Boolean,
    val recordingEngineReady: Boolean = false,
    val twoWayAudioVerified: Boolean = false
)
