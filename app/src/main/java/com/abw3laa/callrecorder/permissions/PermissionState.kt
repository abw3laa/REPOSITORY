package com.abw3laa.callrecorder.permissions

data class PermissionState(
    val phoneStateGranted: Boolean,
    val callLogGranted: Boolean,
    val notificationsGranted: Boolean
) {
    val readyForCallDetection: Boolean get() = phoneStateGranted && callLogGranted
}
