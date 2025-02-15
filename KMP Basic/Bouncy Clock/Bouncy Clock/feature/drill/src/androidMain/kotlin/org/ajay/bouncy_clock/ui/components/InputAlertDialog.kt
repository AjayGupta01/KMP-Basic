package org.ajay.bouncy_clock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun InputAlertDialog(
    title: String,
    placeholderValue: String,
    style: TextStyle = LocalTextStyle.current,
    keyBoardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    var inputValue by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title, style = style.copy(fontWeight = FontWeight.SemiBold))
        },
        text = {
            OutlinedTextField(
                value = inputValue,
                onValueChange = {
                    inputValue = it
                },
                placeholder = {
                    Text(text = placeholderValue, style = style)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyBoardType,
                    autoCorrectEnabled = true
                )
            )
        },
        confirmButton = {
            Button(
                enabled = inputValue.isNotEmpty() && inputValue.length < 20,
                onClick = {
                    onConfirmClick(inputValue.trim())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03D70B),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Confirm", modifier = Modifier.padding(8.dp), style = style)
            }
        }
    )
}