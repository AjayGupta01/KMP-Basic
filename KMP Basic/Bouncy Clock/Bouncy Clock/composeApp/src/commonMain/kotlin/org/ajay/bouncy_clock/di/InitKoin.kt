package org.ajay.bouncy_clock.di

import org.ajay.bouncy_clock.dataStoreModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            platformModuleDi, timerPlatformModule, dataStoreModule, timerCommonModule, roomModule,
            roomPlatformModule, drillPlatformModule
        )
    }
}