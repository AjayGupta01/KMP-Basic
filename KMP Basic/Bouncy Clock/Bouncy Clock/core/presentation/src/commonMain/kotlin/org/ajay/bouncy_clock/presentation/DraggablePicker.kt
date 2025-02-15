package org.ajay.bouncy_clock.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DraggablePicker(
    hourSuffix: String = "h",
    minuteSuffix: String = "m",
    secondSuffix: String = "s",
    backgroundColor: Color = Color(0xFF3F3F3F),
    lineColor: Color = Color(0xFFdb660d),
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onSecondChange: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Picker(
                list = (0..47).map {
                    it%24
                },
                onValueChanged = onHourChange,
                suffix = hourSuffix,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 6
            )
            Picker(
                (0..119).map {
                    it%60
                },
                onValueChanged = onMinuteChange,
                suffix = minuteSuffix,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 6
            )
            Picker(
                list = (0..119).map {
                    it%60
                },
                onValueChanged = onSecondChange,
                suffix = secondSuffix,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                showAmount = 6
            )
        }
        RectanglePicker(
            lineColor = lineColor,
            modifier = Modifier
                .height(260.dp)
                .width(50.dp)
        )
    }
}


@Composable
private fun RectanglePicker(
    modifier: Modifier = Modifier,
    lineColor: Color
) {
    Canvas(
        modifier = modifier
    ) {
        drawRoundRect(
            size = Size(size.width, size.height),
            style = Stroke(width = 10f, join = StrokeJoin.Round),
            color = lineColor,
            cornerRadius = CornerRadius(25f, 25f)
        )
    }
}


@Composable
private fun <T> Picker(
    list: List<T>,
    suffix: String = "",
    showAmount: Int = 10,
    modifier: Modifier = Modifier,
    style: PickerStyle = PickerStyle(),
    onValueChanged: (T) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val listCount = list.size
    val correctionValue = if (list.size % 2 == 0) 1 else 0

    var dragStartedX by remember { mutableFloatStateOf(0f) }
    var currentDragX by remember { mutableFloatStateOf(0f) }
    var oldX by remember { mutableFloatStateOf(0f) }

    val spacePerItem = remember { mutableFloatStateOf(0f) }
    val canvasSize = remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(canvasSize.value) {
        spacePerItem.floatValue = canvasSize.value.width.toFloat() / showAmount
    }

    val textStyle = LocalTextStyle.current
    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragStartedX = offset.x
                    },
                    onDragEnd = {
                        val rest = currentDragX % spacePerItem.floatValue
                        val roundUp = abs(rest / spacePerItem.floatValue).roundToInt() == 1
                        val newX = if (roundUp) {
                            if (rest < 0) currentDragX + abs(rest) - spacePerItem.floatValue
                            else currentDragX - rest + spacePerItem.floatValue
                        } else {
                            if (rest < 0) currentDragX + abs(rest) else currentDragX - rest
                        }
                        currentDragX = newX.coerceIn(
                            -(listCount / 2f) * spacePerItem.floatValue,
                            (listCount / 2f - correctionValue) * spacePerItem.floatValue
                        )
                        val index = (listCount / 2) + (currentDragX / spacePerItem.floatValue).toInt()
                        onValueChanged(list[index])
                        oldX = currentDragX
                    },
                    onDrag = { change, _ ->
                        val newX = oldX + (dragStartedX - change.position.x)
                        currentDragX = newX.coerceIn(
                            -(listCount / 2f) * spacePerItem.floatValue,
                            (listCount / 2f - correctionValue) * spacePerItem.floatValue
                        )
                        val index = (listCount / 2) + (currentDragX / spacePerItem.floatValue).toInt()
                        onValueChanged(list[index])
                    }
                )
            }
            .onSizeChanged { canvasSize.value = it }
    ) {
        // Draw background
        drawRect(
            color = Color(0xFFE5E5E5).copy(alpha = 0.8f),
            topLeft = Offset(-2000f, 0f),
            size = Size(size.width + 4000f, size.height)
        )

        // Draw items
        val spaceForEachItem = size.width / showAmount
        for (i in list.indices) {
            val currentX = i * spaceForEachItem - currentDragX -
                    ((listCount - 1 + correctionValue - showAmount) / 2 * spaceForEachItem)

            // Draw line
            drawLine(
                color = style.lineColor,
                strokeWidth = 1.5.dp.toPx(),
                start = Offset(currentX, 0f),
                end = Offset(currentX, style.lineLength)
            )

            // Draw text
            val text = "${list[i]}$suffix"
            val textLayoutResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = textStyle.copy(
                    fontSize = style.textSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    currentX - (textLayoutResult.size.width / 2),
                    style.lineLength + 5.dp.toPx()
                ),
                brush = SolidColor(Color.Black)
            )
        }
    }
}

private data class PickerStyle(
    val lineColor: Color = Color(0xFFdb660d),
    val lineLength: Float = 45f,
    val textSize: TextUnit = 16.sp
)

