package com.abw3laa.callrecorder.call

data class CallerIdentity(
    val name: String?,
    val number: String?
) {
    val displayName: String
        get() = name?.takeIf { it.isNotBlank() } ?: number ?: "رقم غير معروف"
}
