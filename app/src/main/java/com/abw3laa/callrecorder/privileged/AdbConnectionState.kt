package com.abw3laa.callrecorder.privileged

sealed interface AdbConnectionState {
    data object Disconnected : AdbConnectionState
    data object Connecting : AdbConnectionState
    data class Connected(val endpoint: AdbEndpoint) : AdbConnectionState
    data class Failed(val reason: String) : AdbConnectionState
}
