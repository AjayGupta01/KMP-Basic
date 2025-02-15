package org.ajay.bouncy_clock.app

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object MainGraph : Routes

    @Serializable
    data object HomeScreen : Routes

    @Serializable
    data object TimerScreen : Routes

    @Serializable
    data object DrillMainScreen : Routes

    @Serializable
    data object DrillListScreen : Routes

    @Serializable
    data object DrillDetailScreen : Routes
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) =
    navigate(route = Routes.HomeScreen, navOptions = navOptions)

fun NavController.navigateToTimerScreen(navOptions: NavOptions? = null) =
    navigate(route = Routes.TimerScreen, navOptions = navOptions)

fun NavController.navigateToDrillScreen(navOptions: NavOptions? = null) =
    navigate(route = Routes.DrillMainScreen, navOptions = navOptions)
