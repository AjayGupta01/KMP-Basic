package org.ajay.bouncy_clock.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ajay.bouncy_clock.presentation.FlipCardItem
import org.ajay.bouncy_clock.presentation.TimeSelectionBottomDrawerSheet
import org.ajay.bouncy_clock.presentation.Utility.pad
import org.ajay.bouncy_clock.service.TimerServiceUtility.triggeredForegroundService
import org.ajay.bouncy_clock.service.TimerService
import org.ajay.bouncy_clock.service.TimerState
import org.ajay.bouncy_clock.ui.components.PresetsBottomDrawerSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimerScreen(
    timerService: TimerService,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = koinViewModel()
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val context = LocalContext.current
    val presets = timerViewModel.presetTimes.collectAsStateWithLifecycle()
    var isOpenTagSheet by rememberSaveable { mutableStateOf(false) }
    var isOpenTimeSelectSheet by rememberSaveable { mutableStateOf(false) }
    var showDeleteButton by rememberSaveable { mutableStateOf(false) }

    var hours = 0
    var minutes = 0
    var seconds = 0

    TimerBaseScreen(
        timerService = timerService,
        orientation = orientation,
        context = context,
        modifier = modifier,
        onCardClick = {
            when (timerService.timerState) {
                TimerState.CANCELLED -> {
                    isOpenTagSheet = true
                }

                TimerState.RUNNING -> {
                    triggeredForegroundService(context, TimerState.PAUSED.name)
                }

                TimerState.PAUSED -> {
                    triggeredForegroundService(context, TimerState.RUNNING.name)
                }

                else -> {}
            }
        }
    )
    if (isOpenTagSheet) {
        PresetsBottomDrawerSheet(
            presets = presets.value,
            title = "Select Preset",
            showEditButton = presets.value.isNotEmpty(),
            showDeleteButton = showDeleteButton,
            onDismissDialog = {
                isOpenTagSheet = false
                showDeleteButton = false
            },
            onPresetClick = { index ->
                triggeredForegroundService(
                    context = context,
                    action = TimerState.RUNNING.name,
                    time = presets.value[index]
                )
                isOpenTagSheet = false
            },
            onAddClick = {
                showDeleteButton = false
                isOpenTimeSelectSheet = true
            },
            onEditClick = {
                showDeleteButton = !showDeleteButton
            },
            onDeleteClick = { index ->
                timerViewModel.deletePresetTimer(presets.value[index])
            }
        )
    }
    if (isOpenTimeSelectSheet) {
        TimeSelectionBottomDrawerSheet(
            title = "Select time",
            onDismissRequest = {
                isOpenTimeSelectSheet = false
            },
            onHourChange = {
                hours = it
            },
            onMinuteChange = {
                minutes = it
            },
            onSecondChange = {
                seconds = it
            },
            onSelectedTimeAddClick = {
                if ((hours + minutes + seconds) > 0) {
                    val time =
                        if (hours == 0) {
                            "${minutes.pad()}:${seconds.pad()}"
                        } else {
                            "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"
                        }

                    timerViewModel.addPresetTime(time)
                    isOpenTimeSelectSheet = false
                }
            }
        )
    }
}


@Composable
internal fun TimerBaseScreen(
    timerService: TimerService,
    orientation: Int,
    context: Context,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onCardClick: () -> Unit
) {
    val hour = timerService.hours
    val minute = timerService.minutes
    val second = timerService.seconds
    val currentState = timerService.timerState
    val title = when (currentState) {
        TimerState.PAUSED -> "Timer Paused"
        TimerState.COMPLETED -> "Timer Completed"
        else -> "Timer"
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
                        title = title,
                        mainCardValue = if (currentState == TimerState.COMPLETED) "-$hour" else hour,
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
                        title = title,
                        mainCardValue = if (currentState == TimerState.COMPLETED) "-$minute" else minute,
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
            AnimatedVisibility(visible = currentState != TimerState.RUNNING) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    TimerScreenButtons(context = context, currentState = currentState)
                }
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(8.dp), //different
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp) //different
            ) {
                if (hour != "00") {
                    FlipCardItem(
                        title = title,
                        mainCardValue = if (currentState == TimerState.COMPLETED) "-$hour" else hour,
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
                        title = title,
                        mainCardValue = if (currentState == TimerState.COMPLETED) "-$minute" else minute,
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
            AnimatedVisibility(visible = currentState != TimerState.RUNNING) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimerScreenButtons(context = context, currentState = currentState)
                }
            }
        }
    }

}

@Composable
internal fun TimerScreenButtons(context: Context, currentState: TimerState) {
    AnimatedVisibility(visible = currentState != TimerState.CANCELLED) {
        IconButton(
            onClick = {                                    //cancel button
                triggeredForegroundService(
                    context = context,
                    action = TimerState.CANCELLED.name
                )
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }


    AnimatedVisibility(visible = currentState != TimerState.COMPLETED && currentState != TimerState.CANCELLED) {
        IconButton(
            // play pause button
            onClick = {
                triggeredForegroundService(
                    context = context,
                    action = if (currentState == TimerState.RUNNING) TimerState.PAUSED.name else TimerState.RUNNING.name
                )
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = if (currentState == TimerState.RUNNING) Color.Red else MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.LightGray, shape = CircleShape)
                .background(Color.White),
        ) {
            val icon =
                if (currentState == TimerState.RUNNING) Icons.Filled.Pause else Icons.Filled.PlayArrow
            AnimatedContent(targetState = icon, label = "play pause animation") {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Composable
fun CompletionMessage(
    message: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = style.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize)
        )
    }
}