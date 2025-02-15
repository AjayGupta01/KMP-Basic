package org.ajay.bouncy_clock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun HsvColorPickerDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    onSubmitClick: (String, Color) -> Unit
) {
    val controller = rememberColorPickerController()
    var hexCode by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Transparent) }
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = title,
                    style = style.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                )
                AlphaTile(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    controller = controller,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(250.dp)) {
                    HsvColorPicker(
                        modifier = Modifier
                            .padding(10.dp),
                        controller = controller,
                        drawOnPosSelected = {
                            drawColorIndicator(
                                controller.selectedPoint.value,
                                controller.selectedColor.value,
                            )
                        },
                        onColorChanged = { colorEnvelope ->
                            hexCode = colorEnvelope.hexCode
                            color = colorEnvelope.color
                        },
                        initialColor = Color.Red,
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Alpha", style = style.copy(fontWeight = FontWeight.Black))
                    AlphaSlider(
                        modifier = Modifier
                            .testTag("HSV_AlphaSlider")
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(20.dp)
                            .align(Alignment.CenterHorizontally),
                        controller = controller,
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Brightness", style = style.copy(fontWeight = FontWeight.Black))
                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(20.dp)
                            .align(Alignment.CenterHorizontally),
                        controller = controller,
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "HexCode", style = style.copy(fontWeight = FontWeight.Black))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "#$hexCode",
                        style = style,
                        fontSize = 16.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSubmitClick(hexCode, color)
                onDismissRequest()
            }) {
                Text(text = "Select")
            }
        }
    )
}