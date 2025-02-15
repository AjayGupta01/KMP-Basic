package org.ajay.bouncy_clock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionIcon(
    icon: ImageVector,
    backgroundColor: Color,
    tint: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(backgroundColor).padding(8.dp),
        colors = IconButtonDefaults.iconButtonColors(contentColor = tint)
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}
