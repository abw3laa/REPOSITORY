package com.abw3laa.callrecorder.call

import android.content.Context
import android.util.Log
import java.util.UUID

class CallSessionManager(context: Context) {
    private val appContext = context.applicationContext
    private val callerResolver = CallerResolver(appContext)
    private val history = EncryptedCallLogStore(appContext)

    private var state = CallState.IDLE
    private var direction = CallDirection.UNKNOWN
    private var number: String? = null
    private var callerName: String? = null
    private var startedAtMs: Long? = null

    @Synchronized
    fun onSnapshot(snapshot: CallSnapshot) {
        when (snapshot.state) {
            CallState.RINGING -> {
                state = CallState.RINGING
                direction = CallDirection.INCOMING
                number = snapshot.number
                callerName = callerResolver.resolve(number).name
            }
            CallState.OFFHOOK -> {
                if (state == CallState.IDLE) {
                    direction = CallDirection.OUTGOING
                }
                state = CallState.OFFHOOK
                startedAtMs = startedAtMs ?: snapshot.timestampMs
            }
            CallState.IDLE -> {
                val start = startedAtMs
                if (state == CallState.OFFHOOK && start != null) {
                    val identity = callerResolver.resolve(number)
                    history.append(
                        CallRecord(
                            id = UUID.randomUUID().toString(),
                            startedAtMs = start,
                            endedAtMs = snapshot.timestampMs,
                            direction = direction,
                            callerName = callerName ?: identity.name,
                            callerNumber = identity.number ?: number
                        )
                    )
                    Log.i(TAG, "Call session saved: " + identity.displayName)
                }

                state = CallState.IDLE
                direction = CallDirection.UNKNOWN
                number = null
                callerName = null
                startedAtMs = null
            }
        }
    }

    companion object {
        private const val TAG = "CallSessionManager"
    }
}
