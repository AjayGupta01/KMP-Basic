package org.ajay.bouncy_clock

import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


actual class ScreenOrientationProvider(context: Context) {

    private val _orientationFlow = MutableStateFlow(getCurrentOrientation(context))
    actual val orientationFlow = _orientationFlow.asStateFlow()

    init {
        val callback = object : android.content.ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration) {
                _orientationFlow.value = if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    ScreenOrientation.LANDSCAPE
                } else {
                    ScreenOrientation.PORTRAIT
                }
            }

            override fun onLowMemory() {}
        }

        context.registerComponentCallbacks(callback)
    }

    private fun getCurrentOrientation(context: Context): ScreenOrientation {
        return if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScreenOrientation.LANDSCAPE
        } else {
            ScreenOrientation.PORTRAIT
        }
    }
}

