package com.abw3laa.callrecorder.call

import android.content.Context

object CallSessionRegistry {
    @Volatile private var instance: CallSessionManager? = null
    fun get(context: Context): CallSessionManager =
        instance ?: synchronized(this) {
            instance ?: CallSessionManager(context.applicationContext).also { instance = it }
        }
}
