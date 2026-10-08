package com.abw3laa.callrecorder.privileged

/**
 * Safe default until a reviewed embedded-ADB implementation is integrated.
 * It intentionally does not attempt arbitrary shell access.
 */
class UnsupportedAdbTransport : AdbTransport {
    override var state: AdbConnectionState = AdbConnectionState.Disconnected
        private set

    override suspend fun connect(endpoint: AdbEndpoint): Result<Unit> {
        state = AdbConnectionState.Failed(
            "Embedded ADB transport is not integrated on this build."
        )
        return Result.failure(UnsupportedOperationException(state.toString()))
    }

    override suspend fun disconnect() {
        state = AdbConnectionState.Disconnected
    }

    override suspend fun pushBootstrap(
        bootstrap: RecorderBootstrap
    ): Result<Unit> {
        return Result.failure(
            UnsupportedOperationException("ADB transport is unavailable.")
        )
    }

    override suspend fun closeRecorder(): Result<Unit> {
        return Result.failure(
            UnsupportedOperationException("ADB transport is unavailable.")
        )
    }
}
