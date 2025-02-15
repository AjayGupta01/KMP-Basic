package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.ScreenOrientationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModuleDi: Module
    get() = module {
        single { ScreenOrientationProvider(androidContext()) }

    }