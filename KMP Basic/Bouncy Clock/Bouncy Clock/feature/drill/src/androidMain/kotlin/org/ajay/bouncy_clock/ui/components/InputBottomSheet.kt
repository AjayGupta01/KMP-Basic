package org.ajay.bouncy_clock.ui.components


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.ajay.bouncy_clock.domain.DrillInputSheet
import org.ajay.bouncy_clock.domain.toDrillTimerEntity
import org.ajay.bouncy_clock.ui.drill_list.InputBottomSheetAction
import org.ajay.bouncy_clock.model.DrillTimerEntity
import org.ajay.bouncy_clock.presentation.BottomDrawerSheet
import org.ajay.bouncy_clock.presentation.TimeSelectionBottomDrawerSheet
import org.ajay.bouncy_clock.presentation.Utility.pad
import org.ajay.bouncy_clock.presentation.Utility.toFormattedDuration
import org.ajay.bouncy_clock.presentation.Utility.toTitleCase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InputBottomSheet(
    context: Context,
    drillInput: DrillInputSheet,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAddOrUpdateDrillClick: (DrillTimerEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isOpenInputAlertDialog by rememberSaveable { mutableStateOf(false) }
    var isOpenColorPickerDialog by rememberSaveable { mutableStateOf(false) }
    var isOpenTimeSelectSheet by rememberSaveable { mutableStateOf(false) }
    var action by rememberSaveable { mutableStateOf(InputBottomSheetAction.NAME_TAG) }

    var hours = 0
    var minutes = 0
    var seconds = 0

    BottomDrawerSheet(
        sheetState = sheetState,
        modifier = modifier,
        title = "Select Tag",
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputSheetItem(
                key = "Name",
                value = drillInput.name.toString().toTitleCase(),
                onClick = {
                    action = InputBottomSheetAction.NAME_TAG
                    isOpenInputAlertDialog = true
                }
            )
            InputSheetItem(
                key = "Drill duration",
                value = drillInput.duration.toFormattedDuration(),
                onClick = {
                    action = InputBottomSheetAction.DURATION
                    isOpenTimeSelectSheet = true
                }
            )
            InputSheetItem(
                key = "Color",
                isIconButton = true,
                colorCode = drillInput.colorCode,
                onClick = {
                    action = InputBottomSheetAction.COLOR
                    isOpenColorPickerDialog = true
                }
            )
            InputSheetItem(
                key = "Break duration",
                value = drillInput.breakDuration.toFormattedDuration(),
                onClick = {
                    action = InputBottomSheetAction.SHORT_BREAK
                    isOpenTimeSelectSheet = true
                }
            )
            InputSheetItem(
                key = "Laps", value = drillInput.laps,
                onClick = {
                    action = InputBottomSheetAction.LAP
                    isOpenInputAlertDialog = true
                }
            )
            FloatingActionButton(
                onClick = {
                    if (drillInput.name == null) {
                        Toast.makeText(context, "Please enter drill name", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        onAddOrUpdateDrillClick(drillInput.toDrillTimerEntity())
                    }
                },
                containerColor = Color(0xFFFFC107),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
            }
        }
    }

    if (isOpenColorPickerDialog) {
        HsvColorPickerDialog(
            onDismissRequest = {
                isOpenColorPickerDialog = false
            },
            title = "Select Color"
        ) { colorCode, _ ->
            drillInput.colorCode = colorCode
        }
    }

    if (isOpenTimeSelectSheet) {
        TimeSelectionBottomDrawerSheet(
            title = if (action == InputBottomSheetAction.DURATION) "Select duration" else "Select break",
            onHourChange = {
                hours = it
            },
            onMinuteChange = {
                minutes = it
            },
            onSecondChange = {
                seconds = it
            },
            onDismissRequest = {
                isOpenTimeSelectSheet = false
            },
            onSelectedTimeAddClick = {
                val totalTime =
                    hours + minutes + seconds
                val condition =
                    if (action == InputBottomSheetAction.SHORT_BREAK) totalTime >= 0 else totalTime > 0
                if (condition) {
                    val time = when {
                        hours == 0 -> {
                            "${minutes.pad()}:${seconds.pad()}"
                        }

                        else -> {
                            "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"
                        }
                    }
                    when (action) {
                        InputBottomSheetAction.DURATION -> {
                            drillInput.duration = time
                        }

                        else -> {
                            drillInput.breakDuration = time
                        }
                    }
                    isOpenTimeSelectSheet = false
                }
            }
        )
    }
    if (isOpenInputAlertDialog) {
        InputAlertDialog(
            title = if (action == InputBottomSheetAction.LAP) "Laps" else "Drill name",
            placeholderValue = if (action == InputBottomSheetAction.LAP) "number of lap" else "drill name",
            keyBoardType = if (action == InputBottomSheetAction.LAP) KeyboardType.Number else KeyboardType.Text,
            onDismissRequest = {
                isOpenInputAlertDialog = false
            },
            onConfirmClick = { value ->
                when (action) {
                    InputBottomSheetAction.NAME_TAG -> {
                        drillInput.name = value
                    }

                    else -> {
                        when {
                            value.toDouble() > 100 -> {
                                drillInput.laps = "100"
                            }

                            value.toDouble() == 0.0 -> {
                                drillInput.laps = "1"
                            }

                            else -> {
                                drillInput.laps = value
                            }
                        }
                    }
                }
                isOpenInputAlertDialog = false
            }
        )
    }
}


@Composable
private fun InputSheetItem(
    key: String,
    value: String = "",
    modifier: Modifier = Modifier,
    isIconButton: Boolean = false,
    colorCode: String? = "ffff824b",
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val color = Color(android.graphics.Color.parseColor("#$colorCode"))
            Text(key, style = style)
            if (isIconButton) {
                IconButton(onClick = onClick) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            } else {
                TextButton(onClick = onClick) {
                    Text(
                        text = value,
                        style = style.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    )
                }
            }
        }
    }
}
