package org.ajay.bouncy_clock

import kotlinx.coroutines.flow.StateFlow


expect class ScreenOrientationProvider {
    val orientationFlow: StateFlow<ScreenOrientation>
}


enum class ScreenOrientation {
    PORTRAIT, LANDSCAPE
}
