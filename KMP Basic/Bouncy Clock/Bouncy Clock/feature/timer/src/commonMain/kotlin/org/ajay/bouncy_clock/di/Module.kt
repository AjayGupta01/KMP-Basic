package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.TimerDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

expect val timerPlatformModule: Module
val timerCommonModule = module{

    single{
        TimerDataStore(get())
    }
}