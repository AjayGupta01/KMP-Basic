package org.ajay.bouncy_clock.presentation


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import org.ajay.bouncy_clock.presentation.animation.bouncy.bouncyAnimation
import kotlin.let


@Composable
fun FlipCardItem(
    title: String? = null,
    showHelpingCard: Boolean = false,
    mainCardValue: String,
    helpingCardValue: String? = null,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    ),
    cardColor: Color = Color.White,
    textColor: Color = Color.Black,
    dividerColor: Color = Color.Transparent,
    mainTextDenominatorValue: Float = 1.7f,
    cornerSize: Dp = 40.dp,
    cardElevation: CardElevation = CardDefaults.cardElevation(8.dp),
    onClick: () -> Unit = {}
) {

    BoxWithConstraints(modifier = modifier) { // Fill allocated space
        val s = this.maxWidth
        val cardSize = min(maxWidth, maxHeight) // Make card square and fit within constraints
        val mainCardSize = cardSize // Adjust proportionally
        val mainValueSize = mainCardSize.value / mainTextDenominatorValue
        val helpingCardSize = mainCardSize / 6f
        val helpingValueSize = helpingCardSize.value / 2.2f

        Box(modifier = Modifier.wrapContentSize()) {
            CardItem(
                title = title,
                value = mainCardValue,
                cardSize = mainCardSize,
                valueSize = mainValueSize.sp,
                textStyle = textStyle,
                cardColor = cardColor,
                textColor = textColor,
                dividerColor = dividerColor,
                cornerSize = cornerSize,
                cardElevation = cardElevation,
                onClick = onClick
            )
            if (showHelpingCard) {
                CardItem(
                    value = helpingCardValue,
                    cardSize = helpingCardSize,
                    valueSize = helpingValueSize.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .graphicsLayer {
                            this.translationX = -40f
                            this.translationY = -30f
                        },
                    textStyle = textStyle,
                    cardColor = cardColor,
                    textColor = textColor,
                    dividerColor = Color.Transparent,
                    cornerSize = 16.dp,
                    cardElevation = cardElevation,
                    onClick = {}
                )
            }
        }
    }
}


@Composable
fun CardItem(
    title: String? = null,
    value: String? = null,
    cardSize: Dp,
    valueSize: TextUnit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    cardColor: Color,
    textColor: Color,
    dividerColor: Color,
    cornerSize: Dp,
    cardElevation: CardElevation,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .size(cardSize)
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(cornerSize),
        elevation = cardElevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            title?.let {
                Text(
                    text = it,
                    style = textStyle.copy(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Black,
                        color = textColor
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                )
            }
            value?.let { mainValue ->
                AnimatedContent(
                    contentAlignment = Alignment.Center,
                    targetState = mainValue,
                    label = "main value animation",
                    transitionSpec = { bouncyAnimation() }) {
                    Text(
                        text = it,
                        fontSize = valueSize,
                        style = textStyle.copy(color = textColor)
                    )
                }
            }
            HorizontalDivider(thickness = 4.dp, color = dividerColor)
        }
    }
}

