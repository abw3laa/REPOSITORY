package com.abw3laa.callrecorder.call

data class CallRecord(
    val id: String,
    val startedAtMs: Long,
    val endedAtMs: Long,
    val direction: CallDirection,
    val callerName: String?,
    val callerNumber: String?
) {
    val durationMs: Long
        get() = (endedAtMs - startedAtMs).coerceAtLeast(0L)
}
