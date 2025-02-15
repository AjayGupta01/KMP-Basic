package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.DrillDatabase
import org.ajay.bouncy_clock.DrillRecordDataSource
import org.ajay.bouncy_clock.DrillTimerDataSource
import org.ajay.bouncy_clock.dao.DrillRecordDao
import com.example.room.drill.dao.DrillTimerDao
import org.ajay.bouncy_clock.repo.DrillRecordRepository
import org.ajay.bouncy_clock.repo.DrillTimerRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


expect val roomPlatformModule:Module

val roomModule = module {

    single {
        provideDrillTimerDao(get())
    }
    single {
        provideDrillRecordDao(get())
    }

    singleOf(::DrillTimerRepository).bind<DrillTimerDataSource>()
    singleOf(::DrillRecordRepository).bind<DrillRecordDataSource>()
}

private fun provideDrillTimerDao(database: DrillDatabase): DrillTimerDao = database.drillTimerDao()
private fun provideDrillRecordDao(database: DrillDatabase): DrillRecordDao =
    database.drillRecordDao()
