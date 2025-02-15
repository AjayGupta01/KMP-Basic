package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.database.GetPlatformDrillDataBase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val roomPlatformModule: Module
    get() = module {
        single {
            GetPlatformDrillDataBase(get()).getDatabase()
        }
    }