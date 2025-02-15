//package org.ajay.bouncy_clock.ui.components.pie_chart
//
//import android.graphics.Typeface
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ExperimentalLayoutApi
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlin.collections.forEach
//
//
//@Composable
//fun PieChart(pieSlices: List<PieSlice>, modifier: Modifier = Modifier) {
//    val pieChartData = PieChartData(
//        slices = getPieSlices(pieSlices),
//        plotType = PlotType.Donut
//    )
//    var selectedSliceLabel by rememberSaveable { mutableStateOf("") }  //todo: place it into viewmodel, value resets if configuration changes, needs to find the main issue, may be due to scaffold
//    val pieChartConfig =
//        PieChartConfig(
//            labelVisible = true,
//            strokeWidth = 120f,
//            labelColor = MaterialTheme.colorScheme.onSecondary,
//            activeSliceAlpha = .9f,
//            isEllipsizeEnabled = true,
//            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
//            isAnimationEnable = true,
//            chartPadding = 32,
//            labelFontSize = 42.sp,
//            backgroundColor = MaterialTheme.colorScheme.secondary,
//        )
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//    ) {
//        DonutPieChart(
//            modifier = Modifier
//                .size(400.dp)
//                .align(Alignment.CenterHorizontally),
//            pieChartData,
//            pieChartConfig
//        ) { slice ->
//            selectedSliceLabel = if (selectedSliceLabel == slice.label) "" else slice.label
//        }
//        PieLegends(data = pieSlices, selectedLabel = selectedSliceLabel)
//        Spacer(Modifier.height(16.dp))
//    }
//}
//
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//private fun PieLegends(modifier: Modifier = Modifier, data: List<PieSlice>, selectedLabel: String) {
//    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        data.forEach {
//            val selected = selectedLabel == it.label
//            Row(
//                modifier = modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .animateContentSize()
//                        .size(if (selected) 40.dp else 30.dp)
//                        .clip(RoundedCornerShape(8.dp))
//                        .background(it.color)
//                )
//                Text(
//                    text = it.label,
//                    style = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.titleMedium.fontSize)
//                )
//                Spacer(Modifier.weight(1f))
//                Text(
//                    text = it.totalActiveDuration,
//                    style = LocalTextStyle.current.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
//                )
//            }
//        }
//    }
//}
//
//private fun getPieSlices(data: List<PieSlice>): List<PieChartData.Slice> {
//    return data.map { pieData ->
//        PieChartData.Slice(
//            label = pieData.label,
//            value = pieData.value,
//            color = pieData.color
//        )
//    }
//}
//
//data class PieSlice(
//    val label: String,
//    val value: Float,
//    val color: Color,
//    val totalActiveDuration: String
//)
