package org.ajay.bouncy_clock.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ItemsCategory(
    headlineStringRes: StringResource,
    cardColor: Color,
    headlineStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        color = MaterialTheme.colorScheme.onSecondary
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        CategoryHeadline(headlineStringRes = headlineStringRes, style = headlineStyle)
        Card(colors = CardDefaults.cardColors(containerColor = cardColor)) {
            Column(content = content)
        }
    }
}

@Composable
private fun CategoryHeadline(headlineStringRes: StringResource, style: TextStyle) {
    Text(
        text = stringResource(headlineStringRes),
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Start
    )
}