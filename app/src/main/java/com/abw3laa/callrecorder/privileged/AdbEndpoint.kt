package com.abw3laa.callrecorder.privileged

data class AdbEndpoint(
    val host: String,
    val port: Int
) {
    init {
        require(host.isNotBlank()) { "ADB host is required" }
        require(port in 1..65535) { "Invalid ADB port" }
    }
}
