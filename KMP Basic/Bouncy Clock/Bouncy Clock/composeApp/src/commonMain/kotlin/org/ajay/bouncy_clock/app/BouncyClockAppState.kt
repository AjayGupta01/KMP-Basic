package org.ajay.bouncy_clock.app


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.ajay.bouncy_clock.app.navigation.TopLevelDestination

@Composable
fun rememberBouncyClockAppState(
    navController: NavHostController = rememberNavController()
): BouncyClockAppState {
    return remember(navController) {
        BouncyClockAppState(navController = navController)
    }
}

@Stable
class BouncyClockAppState(val navController: NavHostController) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Suppress("RestrictedApi") @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(topLevelDestination.route::class) == true
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination
    ) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHomeScreen(topLevelNavOptions)
            TopLevelDestination.TIMER -> navController.navigateToTimerScreen(topLevelNavOptions)
            TopLevelDestination.DRILL -> navController.navigateToDrillScreen(topLevelNavOptions)
        }
    }
}