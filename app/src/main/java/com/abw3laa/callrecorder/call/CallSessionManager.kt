package com.abw3laa.callrecorder.call

import android.content.Context
import android.util.Log

class CallSessionManager(private val context: Context) {
    private var state = CallState.IDLE
    private var direction = CallDirection.UNKNOWN
    private var number: String? = null
    private var startedAtMs: Long? = null

    @Synchronized
    fun onSnapshot(snapshot: CallSnapshot) {
        when (snapshot.state) {
            CallState.RINGING -> {
                state = CallState.RINGING
                direction = CallDirection.INCOMING
                number = snapshot.number
            }
            CallState.OFFHOOK -> {
                if (state == CallState.IDLE) direction = CallDirection.OUTGOING
                state = CallState.OFFHOOK
                startedAtMs = startedAtMs ?: snapshot.timestampMs
            }
            CallState.IDLE -> {
                if (state == CallState.OFFHOOK) {
                    Log.i(TAG, "Call session ended; recorder integration is intentionally deferred.")
                }
                state = CallState.IDLE
                direction = CallDirection.UNKNOWN
                number = null
                startedAtMs = null
            }
        }
    }

    companion object {
        private const val TAG = "CallSessionManager"
    }
}
