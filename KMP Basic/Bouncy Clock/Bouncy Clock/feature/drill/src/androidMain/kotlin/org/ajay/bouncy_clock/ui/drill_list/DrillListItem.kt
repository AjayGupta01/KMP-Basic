package org.ajay.bouncy_clock.ui.drill_list


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ajay.bouncy_clock.model.DrillTimerEntity
import org.ajay.bouncy_clock.presentation.Utility.toFormattedDuration

@Composable
internal fun DrillListItem(
    drill: DrillTimerEntity,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Whatshot,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFFFFC107)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = drill.title,
                    style = style.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                )
                Text(
                    text = drill.duration.toFormattedDuration(),
                    style = style.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = Icons.Filled.PlayCircleFilled,
                    contentDescription = null,
                    modifier = Modifier.size(45.dp),
                    tint = Color(0xFF2196F3)
                )
            }
        }
    }

}

