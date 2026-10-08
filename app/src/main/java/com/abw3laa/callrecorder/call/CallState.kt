package com.abw3laa.callrecorder.call

enum class CallState { IDLE, RINGING, OFFHOOK }

enum class CallDirection { INCOMING, OUTGOING, UNKNOWN }

data class CallSnapshot(
    val state: CallState,
    val direction: CallDirection = CallDirection.UNKNOWN,
    val number: String? = null,
    val timestampMs: Long = System.currentTimeMillis()
)
