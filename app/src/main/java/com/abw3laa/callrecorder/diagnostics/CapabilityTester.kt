package com.abw3laa.callrecorder.diagnostics

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class CapabilityTester(private val context: Context) {
    fun inspect(): DeviceCapability {
        fun granted(permission: String): Boolean =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        return DeviceCapability(
            apiLevel = Build.VERSION.SDK_INT,
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            phoneStatePermission = granted(Manifest.permission.READ_PHONE_STATE),
            recordAudioPermission = granted(Manifest.permission.RECORD_AUDIO),
            notificationsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                granted(Manifest.permission.POST_NOTIFICATIONS)
        )
    }
}
