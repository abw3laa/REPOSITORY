package com.abw3laa.callrecorder.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_PHONE_STATE_CHANGED) return
        // TODO: Delegate to CallSessionManager.
    }
}
