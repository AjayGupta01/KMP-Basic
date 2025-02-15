package org.ajay.bouncy_clock.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class FlipAppNotificationManager(
    private val context: Context,
    private val serviceClass: Class<out Service>
) {
    private val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

    fun builder(
        channelId: String,
        title: String,
        message: String,
        smallIcon: Int
    ): NotificationCompat.Builder {
        return NotificationCompat
            .Builder(
                context,
                channelId
            )
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }


    fun clickPendingIntent(clickRequestCode: Int): PendingIntent {
        val clickIntent = Intent("com.example.flipclock.ACTION_OPEN_MAIN_ACTIVITY").apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            context,
            clickRequestCode,
            clickIntent,
            flag
        )
    }

    fun resumePendingIntent(action: String, resumeRequestCode: Int): PendingIntent {
        return notificationPendingIntent(action = action, actionRequestCode = resumeRequestCode)
    }


    fun pausePendingIntent(action: String, pauseRequestCode: Int): PendingIntent {
        return notificationPendingIntent(action = action, actionRequestCode = pauseRequestCode)
    }


    fun cancelPendingIntent(action: String, cancelRequestCode: Int): PendingIntent {
        return notificationPendingIntent(action = action, actionRequestCode = cancelRequestCode)
    }

    private fun notificationPendingIntent(action: String, actionRequestCode: Int): PendingIntent {
        val intent = Intent(context, serviceClass).apply {
            this.action = action
        }
        return PendingIntent.getService(
            context,
            actionRequestCode,
            intent,
            flag
        )
    }
}