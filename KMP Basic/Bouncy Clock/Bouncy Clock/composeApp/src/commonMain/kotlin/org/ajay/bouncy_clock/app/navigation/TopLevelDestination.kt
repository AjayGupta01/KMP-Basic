package org.ajay.bouncy_clock.app.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import bouncyclock.composeapp.generated.resources.Res
import bouncyclock.composeapp.generated.resources.drill
import bouncyclock.composeapp.generated.resources.home
import bouncyclock.composeapp.generated.resources.timer
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Gem
import compose.icons.fontawesomeicons.regular.Hourglass
import compose.icons.fontawesomeicons.solid.Gem
import compose.icons.fontawesomeicons.solid.Hourglass
import org.ajay.bouncy_clock.app.Routes
import org.jetbrains.compose.resources.StringResource

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextRes: StringResource,
    val route: Routes
) {
    HOME(
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        iconTextRes = Res.string.home,
        route = Routes.HomeScreen
    ),
    TIMER(
        unselectedIcon = FontAwesomeIcons.Regular.Hourglass,
        selectedIcon = FontAwesomeIcons.Solid.Hourglass,
        iconTextRes = Res.string.timer,
        route = Routes.TimerScreen
    ),
    DRILL(
        unselectedIcon = FontAwesomeIcons.Regular.Gem,
        selectedIcon = FontAwesomeIcons.Solid.Gem,
        iconTextRes = Res.string.drill,
        route = Routes.DrillMainScreen
    )

}

/*
STOPWATCH(
unselectedIcon = FontAwesomeIcons.Regular.Clock,
selectedIcon = FontAwesomeIcons.Solid.Clock,
iconTextRes = Res.string.stopwatch,
route = StopWatchRoute::class
),

SETTINGS(
unselectedIcon = Icons.Outlined.Settings,
selectedIcon = Icons.Filled.Settings,
iconTextRes = Res.string.settings,
route = SettingsMainScreenRoute::class
)*/
