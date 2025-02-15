package org.ajay.bouncy_clock.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ajay.bouncy_clock.presentation.BottomDrawerSheet
import org.ajay.bouncy_clock.presentation.DraggablePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionBottomDrawerSheet(
    title: String,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onSelectedTimeAddClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    BottomDrawerSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        title = title,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            DraggablePicker(
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
                lineColor = MaterialTheme.colorScheme.background,
                onHourChange = onHourChange,
                onMinuteChange = onMinuteChange,
                onSecondChange = onSecondChange
            )
        }
        Spacer(Modifier.height(16.dp))
        FloatingActionButton(
            onClick = onSelectedTimeAddClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
        }
        Spacer(Modifier.height(8.dp))
    }
}
