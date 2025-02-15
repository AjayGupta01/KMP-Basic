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
data object SettingsRoute

@Serializable
data object SettingsMainScreenRoute

@Serializable
data object SettingsFontSelectionRoute

@Serializable
data object SettingsThemeSelectionRoute

val settingsNavOptions = navOptions {
    popUpTo(SettingsRoute) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

fun NavController.navigateToSettings(navOptions: NavOptions = settingsNavOptions) =
    navigate(route = SettingsRoute, navOptions = navOptions)

fun NavController.navigateToSettingsFontSelection() =
    navigate(route = SettingsFontSelectionRoute, navOptions = settingsNavOptions)

fun NavController.navigateToSettingsThemeSelection() =
    navigate(route = SettingsThemeSelectionRoute, navOptions = settingsNavOptions)



fun NavGraphBuilder.settingsScreen(rootNavController: NavHostController) {
    navigation<SettingsRoute>(startDestination = SettingsMainScreenRoute) {
        composable<SettingsMainScreenRoute> {
            SettingsScreen()
        }
        composable<SettingsFontSelectionRoute> {
            FontSelectionScreen()
        }
        composable<SettingsThemeSelectionRoute> {
            ThemeSelectionScreen()
        }

    }
}


@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text("settings")
    }
}
@Composable
fun FontSelectionScreen(modifier: Modifier = Modifier) {

}

@Composable
fun ThemeSelectionScreen(modifier: Modifier = Modifier) {

}