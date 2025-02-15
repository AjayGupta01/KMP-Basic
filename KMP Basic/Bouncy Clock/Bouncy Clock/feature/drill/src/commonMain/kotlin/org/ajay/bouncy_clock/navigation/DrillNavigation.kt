package org.ajay.bouncy_clock.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object DrillRoute

@Serializable
data object DrillListRoute

@Serializable
data object DrillDetailRoute

@Serializable
data object ReportRoute

val drillNavOptions = navOptions {
    popUpTo(DrillRoute) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}


fun NavController.navigateToDrillDetailScreen() {
    navigate(route = DrillDetailRoute, navOptions = drillNavOptions)
}

fun NavController.navigateToReportScreen() {
    navigate(route = ReportRoute, navOptions = drillNavOptions)
}

fun NavGraphBuilder.drillScreen() {
    navigation<DrillRoute>(startDestination = DrillListRoute) {
        composable<DrillListRoute> {
            DrillScreen()
        }
        composable<DrillDetailRoute> {
            DrillDetailScreen()
        }
        composable<ReportRoute> {
            ReportScreen()
        }
    }
}

@Composable
fun DrillScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text("drill")
    }
}
@Composable
fun DrillDetailScreen(modifier: Modifier = Modifier) {

}

@Composable
fun ReportScreen(modifier: Modifier = Modifier) {

}

