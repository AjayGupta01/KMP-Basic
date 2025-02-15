package org.ajay.bouncy_clock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ajay.bouncy_clock.presentation.BottomDrawerSheet
import org.ajay.bouncy_clock.presentation.Utility.getRandomDarkColor


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun PresetsBottomDrawerSheet(
    presets: List<String>,
    title: String,
    style: TextStyle = LocalTextStyle.current,
    onDismissDialog: () -> Unit,
    onPresetClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    showEditButton: Boolean,
    onEditClick: () -> Unit,
    showDeleteButton: Boolean,
    onDeleteClick: (Int) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BottomDrawerSheet(
        onDismissRequest = onDismissDialog,
        sheetState = sheetState,
        title = title,
        showEditButton = showEditButton,
        showDeleteButton = showDeleteButton,
        onEditClick = onEditClick
    ) {
        LazyColumn(modifier = Modifier.height(250.dp)) {
            if (presets.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No data found",
                            style = style.copy(fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                        )
                    }
                }
            } else {
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        maxItemsInEachRow = 3
                    ) {
                        presets.forEachIndexed { index, preset ->
                            AssistChip(
                                onClick = {
                                    onPresetClick(index)
                                },
                                label = {
                                    Text(
                                        text = preset,
                                        style = style.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                                        ),
                                        modifier = Modifier.padding(16.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (showDeleteButton) {
                                        IconButton(
                                            onClick = {
                                                onDeleteClick(index)
                                            }
                                        ) {
                                            Icon(Icons.Filled.Delete, contentDescription = null)
                                        }
                                    }
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = getRandomDarkColor(),
                                    labelColor = Color.White,
                                    trailingIconContentColor = Color.White
                                )
                            )

                        }
                    }
                }
            }


        }
        Spacer(Modifier.height(16.dp))
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Color(0xFFFFC107),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
        Spacer(Modifier.height(16.dp))
    }
}

