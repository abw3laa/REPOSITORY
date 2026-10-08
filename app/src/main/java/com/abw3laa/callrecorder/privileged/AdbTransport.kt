package com.abw3laa.callrecorder.privileged

/**
 * Minimal transport boundary for the future embedded ADB implementation.
 *
 * Implementations must never expose arbitrary shell execution to higher layers.
 * Only the recorder bootstrap protocol may be transported through this boundary.
 */
interface AdbTransport {
    val state: AdbConnectionState

    suspend fun connect(endpoint: AdbEndpoint): Result<Unit>
    suspend fun disconnect()

    suspend fun pushBootstrap(
        bootstrap: RecorderBootstrap
    ): Result<Unit>

    suspend fun closeRecorder(): Result<Unit>
}

data class RecorderBootstrap(
    val sessionId: String,
    val bootstrapSecret: ByteArray,
    val recorderPayload: ByteArray
) {
    init {
        require(sessionId.isNotBlank())
        require(bootstrapSecret.size == 32)
        require(recorderPayload.isNotEmpty())
    }
}
