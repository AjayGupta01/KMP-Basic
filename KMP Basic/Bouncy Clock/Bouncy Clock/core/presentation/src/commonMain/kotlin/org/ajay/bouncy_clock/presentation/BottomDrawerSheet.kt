package org.ajay.bouncy_clock.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomDrawerSheet(
    sheetState: SheetState,
    modifier: Modifier = Modifier,
    title: String,
    style: TextStyle = LocalTextStyle.current,
    showEditButton: Boolean = false,
    showDeleteButton: Boolean = false,
    onEditClick: () -> Unit = {},
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    ModalBottomSheet(
        sheetState = sheetState,
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = ModalBottomSheetProperties(shouldDismissOnBackPress = true),
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = style.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )
                    if (showEditButton) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = if (showDeleteButton) Icons.Filled.CheckCircle else Icons.Filled.Edit,
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
            }
        },
        content = content
    )
}