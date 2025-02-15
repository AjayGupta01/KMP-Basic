package org.ajay.bouncy_clock.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import org.ajay.bouncy_clock.ScreenOrientation
import org.ajay.bouncy_clock.ScreenOrientationProvider
import org.ajay.bouncy_clock.app.navigation.KMPBouncyClockNavHost
import org.ajay.bouncy_clock.app.navigation.TopLevelDestination
import org.ajay.bouncy_clock.app.navigation.isRouteInHierarchy
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.reflect.KClass

@Composable
fun BouncyClock(modifier: Modifier = Modifier) {
    val screenOrientationProvider: ScreenOrientationProvider = koinInject()
    val orientation by screenOrientationProvider.orientationFlow.collectAsState()
    val appState = rememberBouncyClockAppState()
    var isNavBarVisible by remember {
        mutableStateOf(true)
    }
    val dragModifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectVerticalDragGestures(
                onVerticalDrag = { _, dragAmount ->
                    if (dragAmount > 20) {
                        // Drag down - Hide navigation bar
                        isNavBarVisible = false
                    } else if (dragAmount < -20) {
                        // Drag up - Show navigation bar
                        isNavBarVisible = true
                    }
                }
            )
        }

    AdaptiveBottomNavigationLayout(
        modifier = dragModifier,
        isPortrait = orientation == ScreenOrientation.PORTRAIT,
        isNavBarVisible = true,
        bottomNavigation = {
            FlipClockNavigationBar(
                orientation = orientation,
                isNavBarVisible = isNavBarVisible,
                currentDestination = appState.currentDestination,
                topLevelDestination = appState.topLevelDestinations,
                onClick = { destination ->
                    appState.navigateToTopLevelDestination(destination)
                }
            )
        },
        content = {
            KMPBouncyClockNavHost(
                navController = appState.navController
            )
        }
    )
}


@Composable
private fun FlipClockNavigationBar(
    orientation: ScreenOrientation,
    isNavBarVisible: Boolean,
    currentDestination: NavDestination?,
    topLevelDestination: List<TopLevelDestination>,
    modifier: Modifier = Modifier,
    onClick: (TopLevelDestination) -> Unit
) {
    val iconModifier = modifier
        .clip(RoundedCornerShape(24.dp))
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(8.dp)
        .wrapContentWidth()

    AnimatedVisibility(visible = isNavBarVisible) {
        when (orientation) {
            ScreenOrientation.PORTRAIT -> {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = iconModifier
                    ) {
                        NavigationBarButtons(
                            currentDestination = currentDestination,
                            topLevelDestination = topLevelDestination,
                            onClick = onClick
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            else -> {
                Row {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = iconModifier
                    ) {
                        NavigationBarButtons(
                            currentDestination = currentDestination,
                            topLevelDestination = topLevelDestination,
                            onClick = onClick
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }

}


@Composable
private fun NavigationBarButtons(
    currentDestination: NavDestination?,
    topLevelDestination: List<TopLevelDestination>,
    onClick: (TopLevelDestination) -> Unit
) {
    topLevelDestination.forEachIndexed { index, destination ->
        val selected = currentDestination.isRouteInHierarchy(destination.route::class)
        val containerColor =
            if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
        val contentColor =
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        IconButton(
            onClick = {
                onClick(destination)
            },
            colors = IconButtonDefaults.iconButtonColors(contentColor = contentColor)
        ) {
            Icon(
                imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                contentDescription = stringResource(destination.iconTextRes),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(containerColor)
                    .border(
                        width = 2.dp,
                        color = if (selected) contentColor else Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .size(40.dp)
            )
        }
    }
}


@Composable
fun AdaptiveBottomNavigationLayout(
    isPortrait: Boolean,
    modifier: Modifier = Modifier,
    isNavBarVisible: Boolean,
    bottomNavigation: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { insetPadding ->
        Box(modifier = modifier.fillMaxSize().padding(insetPadding)) {
            // Content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = if (isPortrait && isNavBarVisible) 72.dp else 0.dp, // Adjust bottom padding in portrait
                        end = if (!isPortrait && isNavBarVisible) 72.dp else 0.dp // Adjust start padding in landscape
                    )
            ) {
                content()
            }

            // Bottom navigation in portrait, side navigation in landscape
            if (isPortrait) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(72.dp), // Standard bottom bar height
                    contentAlignment = Alignment.Center
                ) {
                    bottomNavigation()
                }
            } else {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(72.dp), // Standard navigation rail width
                    contentAlignment = Alignment.Center
                ) {
                    bottomNavigation()
                }
            }
        }
    }
}
