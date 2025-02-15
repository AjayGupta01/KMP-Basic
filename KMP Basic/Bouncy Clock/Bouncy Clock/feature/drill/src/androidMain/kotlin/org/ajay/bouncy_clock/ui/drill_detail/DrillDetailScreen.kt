package org.ajay.bouncy_clock.ui.drill_detail

import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.ajay.bouncy_clock.presentation.FlipCardItem
import org.ajay.bouncy_clock.service.DrillService
import org.ajay.bouncy_clock.service.DrillServiceUtility.triggeredForegroundService


@Composable
fun DrillDetailScreen(
    rootNavController: NavHostController,
    service: DrillService,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val context = LocalContext.current
    val currentDrillState = service.currentDrillState
    BackHandler {
//        rootNavController.navigateToDrillScreen()
    }

    DrillDetailBaseScreen(
        service = service,
        orientation = orientation,
        context = context,
        modifier = modifier,
        onCardClick = {
            when (service.currentDrillState) {
                DrillState.DRILL_RUNNING -> {
                    triggeredForegroundService(context, DrillState.PAUSED.name)
                }

                DrillState.PAUSED, DrillState.SHORT_BREAK_PAUSED -> {
                    triggeredForegroundService(context, DrillState.DRILL_RUNNING.name)
                }

                DrillState.SHORT_BREAK -> {
                    triggeredForegroundService(context, DrillState.PAUSED.name)
                }

                else -> {}
            }
        }
    )

    LaunchedEffect(currentDrillState) {
        if (currentDrillState == DrillState.CANCELLED) {
//            rootNavController.navigateToDrillScreen()
        }
    }
}

@Composable
internal fun DrillDetailBaseScreen(
    service: DrillService, orientation: Int,
    context: Context,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onCardClick: () -> Unit
) {
    val hour = service.hours
    val minute = service.minutes
    val second = service.seconds
    val currentLap = service.currentServiceDrillInput.currentLap
    val currentState = service.currentDrillState
    val currentTitle = when (currentState) {
        DrillState.PAUSED -> "Paused"
        DrillState.SHORT_BREAK -> "Short-Break"
        DrillState.SHORT_BREAK_PAUSED -> "Short-Break-Paused"
        DrillState.COMPLETED -> "${service.currentServiceDrillInput.title} Completed"
        else -> service.currentServiceDrillInput.title
    }

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp) // Ensures equal spacing
            ) {
                if (hour != "00") {
                    FlipCardItem(
                        title = currentTitle,
                        mainCardValue =  if (currentState == DrillState.COMPLETED) "-$hour" else hour,
                        showHelpingCard = currentState != DrillState.COMPLETED,
                        helpingCardValue = "L$currentLap",
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                    FlipCardItem(
                        mainCardValue = minute,
                        showHelpingCard = true,
                        helpingCardValue = second,
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                } else {
                    FlipCardItem(
                        title = currentTitle,
                        mainCardValue = if (currentState == DrillState.COMPLETED) "-$minute" else minute,
                        showHelpingCard = currentState != DrillState.COMPLETED,
                        helpingCardValue = "L$currentLap",
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                    FlipCardItem(
                        mainCardValue = second,
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                }

            }

            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(visible = currentState != DrillState.DRILL_RUNNING && currentState != DrillState.SHORT_BREAK) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    DrillDetailScreenButtons(
                        context = context
                    )
                }
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)  //different
            ) {
                if (hour != "00") {
                    FlipCardItem(
                        title = currentTitle,
                        mainCardValue = if (currentState == DrillState.COMPLETED) "-$hour" else hour,
                        showHelpingCard = currentState != DrillState.COMPLETED,
                        helpingCardValue = "L$currentLap",
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                    FlipCardItem(
                        mainCardValue = minute,
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                    FlipCardItem(
                        mainCardValue = second,
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                } else {
                    FlipCardItem(
                        title = currentTitle,
                        mainCardValue = if (currentState == DrillState.COMPLETED) "-$minute" else minute,
                        showHelpingCard = currentState != DrillState.COMPLETED,
                        helpingCardValue = "L$currentLap",
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                    FlipCardItem(
                        mainCardValue = second,
                        modifier = Modifier.weight(1f),
                        onClick = onCardClick
                    )
                }

            }

            Spacer(Modifier.width(8.dp))
            AnimatedVisibility(visible = currentState != DrillState.DRILL_RUNNING) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DrillDetailScreenButtons(context = context)
                }
            }
        }
    }
}

@Composable
internal fun DrillDetailScreenButtons(context: Context) {
    IconButton(
        onClick = {                                    //cancel button
            triggeredForegroundService(
                context = context,
                action = DrillState.CANCELLED.name
            )
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .border(width = 2.dp, color = Color.Transparent, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
    }
}