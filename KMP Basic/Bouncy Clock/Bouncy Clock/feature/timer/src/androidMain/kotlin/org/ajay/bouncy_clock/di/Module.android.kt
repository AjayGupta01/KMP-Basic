package org.ajay.bouncy_clock.di

import android.app.NotificationManager
import android.content.Context
import org.ajay.bouncy_clock.notification.FlipAppNotificationManager
import org.ajay.bouncy_clock.service.TimerConstants.CLICK_REQUEST_CODE
import org.ajay.bouncy_clock.service.TimerConstants.TIMER_NOTIFICATION_CHANNEL_ID
import org.ajay.bouncy_clock.service.TimerService
import org.ajay.bouncy_clock.ui.TimerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val timerPlatformModule = module {
    single {
        androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    viewModelOf(::TimerViewModel)

    factory(named("TimerNotificationManager")) {
        FlipAppNotificationManager(
            context = androidContext(),
            serviceClass = TimerService::class.java
        )
    }

    factory(named("TimerNotificationBuilder")) {
        val appNotificationManager: FlipAppNotificationManager =
            get(named("TimerNotificationManager"))
        appNotificationManager.builder(
            channelId = TIMER_NOTIFICATION_CHANNEL_ID,
            title = "Timer",
            message = "00:00:00",
            smallIcon = org.ajay.bouncy_clock.feature.timer.R.drawable.baseline_hourglass_bottom_24
        )
            .setContentIntent(
                appNotificationManager.clickPendingIntent(
                    clickRequestCode = CLICK_REQUEST_CODE
                )
            )
    }
}




