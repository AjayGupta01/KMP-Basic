package org.ajay.bouncy_clock.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.ajay.bouncy_clock.app.Routes
import org.ajay.bouncy_clock.home.HomeScreen
import kotlin.reflect.KClass

@Composable
fun KMPBouncyClockNavHost(navController: NavHostController,modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.MainGraph,
        modifier = modifier
    ) {
        navigation<Routes.MainGraph>(startDestination = Routes.HomeScreen) {
            composable<Routes.HomeScreen> {
                HomeScreen()
            }
            composable<Routes.TimerScreen> {
                DummyTimerScreen()
            }
            drillScreen()
        }
    }
}


fun NavGraphBuilder.drillScreen() {
    navigation<Routes.DrillMainScreen>(startDestination = Routes.DrillListScreen) {
        composable<Routes.DrillListScreen> {
            DummyDrillListScreen()
        }
        composable<Routes.DrillDetailScreen> {
            DummyDrillDetailScreen()
        }
    }
}



@Composable
fun DummyTimerScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "TimerScreen")
    }
}

@Composable
fun DummyDrillListScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "DrillListScreen")
    }
}

@Composable
fun DummyDrillDetailScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "DrillDetailScreen")
    }
}


internal fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
