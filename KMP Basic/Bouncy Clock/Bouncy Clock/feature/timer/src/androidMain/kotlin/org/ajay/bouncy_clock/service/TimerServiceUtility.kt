package org.ajay.bouncy_clock.service

import android.content.Context
import android.content.Intent


object TimerServiceUtility {
    fun triggeredForegroundService(context: Context, action: String, time: String? = null) {
        Intent(context, TimerService::class.java).apply {
            time?.let {
                putExtra(TimerConstants.TIME_VALUE, it)
            }
            this.action = action
            context.startForegroundService(this)
        }
    }
}



