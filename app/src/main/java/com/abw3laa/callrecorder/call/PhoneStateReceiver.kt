package com.abw3laa.callrecorder.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return

        val state = when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
            TelephonyManager.EXTRA_STATE_RINGING -> CallState.RINGING
            TelephonyManager.EXTRA_STATE_OFFHOOK -> CallState.OFFHOOK
            TelephonyManager.EXTRA_STATE_IDLE -> CallState.IDLE
            else -> return
        }

        val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        CallSessionRegistry.get(context).onSnapshot(
            CallSnapshot(state = state, number = number)
        )
    }
}
