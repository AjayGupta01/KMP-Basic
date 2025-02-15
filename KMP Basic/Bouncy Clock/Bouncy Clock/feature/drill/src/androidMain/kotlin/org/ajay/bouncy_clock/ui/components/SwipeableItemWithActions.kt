package org.ajay.bouncy_clock.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    contents: @Composable () -> Unit
) {
    var contextMenuWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Sync animation with state
    LaunchedEffect(isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(-contextMenuWidth) // Animate to the left
        } else {
            offset.animateTo(0f) // Reset to original position
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Actions Row (Displayed on the End Side)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd) // Align actions to the end
                .onSizeChanged { contextMenuWidth = it.width.toFloat() },
            content = actions
        )

        // Main Content with swipe interaction
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    IntOffset(offset.value.roundToInt(), 0)
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-contextMenuWidth, 0f) // Allow swiping to the left only
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offset.value <= -contextMenuWidth / 2f) {
                                    offset.animateTo(-contextMenuWidth) // Fully reveal actions
                                    onExpanded()
                                } else {
                                    offset.animateTo(0f) // Collapse actions
                                    onCollapsed()
                                }
                            }
                        }
                    )
                },
            content = contents
        )
    }
}