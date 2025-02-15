package org.ajay.bouncy_clock.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.ajay.bouncy_clock.ScreenOrientation
import org.ajay.bouncy_clock.ScreenOrientationProvider
import org.ajay.bouncy_clock.home.Utility.toFormattedCurrentTime
import org.ajay.bouncy_clock.home.model.CurrentTime
import org.ajay.bouncy_clock.presentation.FlipCardItem
import org.koin.compose.koinInject


@Composable
fun HomeScreen() {

    val currentTimerZone = TimeZone.currentSystemDefault()
    val localDateTime = Clock.System.now().toLocalDateTime(currentTimerZone)
    var currentTime by remember {
        mutableStateOf(localDateTime)
    }
    val formattedCurrentTime by remember {
        derivedStateOf {
            currentTime.toFormattedCurrentTime()
        }
    }

    val screenOrientationProvider: ScreenOrientationProvider = koinInject()
    val orientation by screenOrientationProvider.orientationFlow.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            withContext(Dispatchers.IO) {
                currentTime = Clock.System.now().toLocalDateTime(currentTimerZone)
            }
            delay(1000L)
        }
    }



    ClockBaseScreen(
        currentTime = formattedCurrentTime,
        is24HourFormat = true,
        orientation = orientation
    )
}


@Composable
internal fun ClockBaseScreen(
    currentTime: CurrentTime,
    is24HourFormat: Boolean,
    modifier: Modifier = Modifier,
    orientation: ScreenOrientation,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    if (orientation == ScreenOrientation.PORTRAIT) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlipCardItem(
                title = currentTime.date,
                mainCardValue = currentTime.hour,
                showHelpingCard = !is24HourFormat,
                helpingCardValue = if (!is24HourFormat) currentTime.amPm else null,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.height(16.dp))
            FlipCardItem(
                title = currentTime.dayName,
                mainCardValue = currentTime.minute,
                showHelpingCard = true,
                helpingCardValue = currentTime.second,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            FlipCardItem(
                title = currentTime.date,
                mainCardValue = currentTime.hour,
                showHelpingCard = !is24HourFormat,
                helpingCardValue = if (!is24HourFormat) currentTime.amPm else null,
                modifier = Modifier.weight(1f)
            )
            FlipCardItem(
                title = currentTime.dayName,
                mainCardValue = currentTime.minute,
                modifier = Modifier.weight(1f),
            )
            FlipCardItem(
                mainCardValue = currentTime.second,
                modifier = Modifier.weight(1f)
            )

        }
    }
}


