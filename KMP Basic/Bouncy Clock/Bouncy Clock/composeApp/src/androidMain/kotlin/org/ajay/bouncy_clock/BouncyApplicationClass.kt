package org.ajay.bouncy_clock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import org.ajay.bouncy_clock.di.initKoin
import org.ajay.bouncy_clock.service.Constants.DRILL_NOTIFICATION_CHANNEL_ID
import org.ajay.bouncy_clock.service.Constants.DRILL_NOTIFICATION_CHANNEL_NAME
import org.ajay.bouncy_clock.service.TimerConstants.TIMER_NOTIFICATION_CHANNEL_ID
import org.ajay.bouncy_clock.service.TimerConstants.TIMER_NOTIFICATION_CHANNEL_NAME
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import kotlin.getValue

class BouncyApplicationClass : Application() {
    private val notificationManager: NotificationManager by inject()
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BouncyApplicationClass)
        }
        createTimerNotificationChannel()
        createDrillNotificationChannel()
    }

    private fun createTimerNotificationChannel() {
        val channel = NotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            TIMER_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
    private fun createDrillNotificationChannel() {
        val channel = NotificationChannel(
            DRILL_NOTIFICATION_CHANNEL_ID,
            DRILL_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

}