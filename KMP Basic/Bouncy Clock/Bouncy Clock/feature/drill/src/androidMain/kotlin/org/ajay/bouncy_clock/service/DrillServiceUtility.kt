package org.ajay.bouncy_clock.service

import android.content.Context
import android.content.Intent
import org.ajay.bouncy_clock.domain.ServiceDrillInput
import org.ajay.bouncy_clock.service.Constants.DRILL_DETAIL_INPUT_KEY

object DrillServiceUtility {
    fun triggeredForegroundService(context: Context, action: String, drill: ServiceDrillInput? = null) {
        Intent(context, DrillService::class.java).apply {
            drill?.let {
                putExtra(DRILL_DETAIL_INPUT_KEY, it)
            }
            this.action = action
            context.startForegroundService(this)
        }
    }
}