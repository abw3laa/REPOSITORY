package com.abw3laa.callrecorder.permissions

import android.Manifest
import android.os.Build

object PermissionPolicy {
    /**
     * Runtime permissions required by the current personal-use app contract.
     *
     * READ_CONTACTS is used only to resolve a caller name from a number.
     * If it is denied, the app continues to identify the caller by number.
     * READ_CALL_LOG remains intentionally excluded.
     */
    val requiredAtRuntime: List<String>
        get() = buildList {
            add(Manifest.permission.READ_PHONE_STATE)
            add(Manifest.permission.READ_CONTACTS)
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
}
