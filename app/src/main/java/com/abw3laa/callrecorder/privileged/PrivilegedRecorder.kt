package com.abw3laa.callrecorder.privileged

interface PrivilegedRecorder {
    suspend fun start(
        sessionId: String,
        outputPath: String
    ): Result<Unit>

    suspend fun stop(): Result<Unit>

    suspend fun selfTest(): Result<Unit>
}
