package com.abw3laa.callrecorder.permissions

import android.Manifest
import android.os.Build

object PermissionPolicy {
    val requiredAtRuntime: List<String>
        get() = buildList {
            add(Manifest.permission.READ_PHONE_STATE)
            add(Manifest.permission.READ_CALL_LOG)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) add(Manifest.permission.POST_NOTIFICATIONS)
        }
}
