package org.ajay.bouncy_clock

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
actual class ScreenOrientationProvider {

    private val _orientationFlow = MutableStateFlow(getCurrentOrientation())
    actual val orientationFlow = _orientationFlow.asStateFlow()

    private val observer = object : NSObject() {
        @Suppress("unused")
        @ObjCAction
        fun onOrientationChanged() {
            _orientationFlow.value = getCurrentOrientation()
        }
    }

    init {
        NSNotificationCenter.defaultCenter.addObserver(
            observer,
            selector = NSSelectorFromString("onOrientationChanged"),
            name = UIApplicationDidChangeStatusBarOrientationNotification,
            `object` = null
        )
    }

    private fun getCurrentOrientation(): ScreenOrientation {
        val screenSize = UIScreen.mainScreen.bounds.useContents { size }
        return if (screenSize.width > screenSize.height) {
            ScreenOrientation.LANDSCAPE
        } else {
            ScreenOrientation.PORTRAIT
        }
    }
}


