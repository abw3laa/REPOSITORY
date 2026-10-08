package com.abw3laa.callrecorder.privileged

sealed interface RecorderCommand {
    data class Start(
        val sessionId: String,
        val outputPath: String
    ) : RecorderCommand

    data object Stop : RecorderCommand
    data object SelfTest : RecorderCommand
}
