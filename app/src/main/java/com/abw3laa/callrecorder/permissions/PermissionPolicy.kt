package com.abw3laa.callrecorder.permissions

import android.Manifest
import android.os.Build

object PermissionPolicy {
    /**
     * Only permissions that are directly required by the current app contract.
     *
     * READ_CALL_LOG is intentionally excluded: Google Play treats Call Log
     * permissions as restricted and basic call-state detection does not require it.
     */
    val requiredAtRuntime: List<String>
        get() = buildList {
            add(Manifest.permission.READ_PHONE_STATE)
            add(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
}
