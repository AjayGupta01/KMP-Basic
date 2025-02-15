package org.ajay.bouncy_clock

import androidx.compose.ui.window.ComposeUIViewController
import org.ajay.bouncy_clock.app.App
import org.ajay.bouncy_clock.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }