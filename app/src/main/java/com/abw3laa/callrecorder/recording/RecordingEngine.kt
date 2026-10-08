package com.abw3laa.callrecorder.recording

interface RecordingEngine {
    val id: String
    suspend fun start(session: RecordingSession): Result<Unit>
    suspend fun stop(): Result<Unit>
    suspend fun selfTest(): RecordingCapability
}

data class RecordingSession(
    val startedAtEpochMs: Long,
    val direction: CallDirection,
    val number: String?
)

enum class CallDirection { INCOMING, OUTGOING, UNKNOWN }

data class RecordingCapability(
    val supported: Boolean,
    val engineId: String,
    val details: String
)
