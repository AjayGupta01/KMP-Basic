package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.DrillRecordDataSource
import org.ajay.bouncy_clock.DrillTimerDataSource
import org.ajay.bouncy_clock.notification.FlipAppNotificationManager
import org.ajay.bouncy_clock.repo.DrillTimerRepository
import org.ajay.bouncy_clock.service.Constants
import org.ajay.bouncy_clock.service.Constants.CLICK_REQUEST_CODE
import org.ajay.bouncy_clock.service.DrillService
import org.ajay.bouncy_clock.ui.drill_list.DrillListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

actual val drillPlatformModule = module {

    viewModelOf(::DrillListViewModel)

    single {
        DrillTimerRepository(get())
    }.bind<DrillTimerDataSource>()

    factory(named("DrillNotificationManager")) {
        FlipAppNotificationManager(
            context = androidContext(),
            serviceClass = DrillService::class.java
        )
    }

    factory(named("DrillNotificationBuilder")) {
        val appNotificationManager: FlipAppNotificationManager = get(named("DrillNotificationManager"))
        appNotificationManager.builder(
            channelId = Constants.DRILL_NOTIFICATION_CHANNEL_ID,
            title = "Drill",
            message = "00:00:00",
            smallIcon = org.ajay.bouncy_clock.feature.drill.R.drawable.baseline_run_circle_24
        )
            .setContentIntent(
                appNotificationManager.clickPendingIntent(
                    clickRequestCode = CLICK_REQUEST_CODE
                )
            )
    }
}