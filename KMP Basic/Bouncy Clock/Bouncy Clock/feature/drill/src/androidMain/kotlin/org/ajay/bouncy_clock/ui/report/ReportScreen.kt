//package com.example.drill.ui.report
//
//import android.content.Context
//import android.widget.Toast
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavHostController
//import com.example.drill.R
//import com.example.drill.navigation.navigateToDrillScreen
//import org.ajay.bouncy_clock.ui.components.EmptyStateMessage
//import org.ajay.bouncy_clock.ui.components.LoadingIndicator
//import org.ajay.bouncy_clock.ui.components.pie_chart.PieChart
//import org.ajay.bouncy_clock.ui.components.pie_chart.PieSlice
//import org.ajay.bouncy_clock.ui.drill_list.DrillListEvent
//import com.example.presentation.ItemsCategory
//import com.example.presentation.ObserveAsEvents
//import com.example.presentation.ScreenHeader
//import org.ajay.bouncy_clock.navigation.navigateToDrillScreen
//import org.ajay.bouncy_clock.presentation.ObserveAsEvents
//import org.ajay.bouncy_clock.presentation.ScreenHeader
//import org.koin.androidx.compose.koinViewModel
//import kotlin.time.Duration.Companion.seconds
//
//@Composable
//fun ReportScreen(
//    modifier: Modifier = Modifier,
//    rootNavController: NavHostController,
//    reportViewModel: ReportViewModel = koinViewModel()
//) {
//    val context = LocalContext.current
//    BackHandler {
//        rootNavController.navigateToDrillScreen()
//    }
//    ObserveAsEvents(reportViewModel.events) { event ->
//        when (event) {
//            is DrillListEvent.Error -> Toast.makeText(
//                context,
//                event.error.toString(),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            is DrillListEvent.Success -> Toast.makeText(
//                context,
//                event.message.toString(),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//    }
//    ReportBaseScreen(
//        context = context,
//        modifier = modifier,
//        reportViewModel = reportViewModel,
//        onActionClick = {
//            rootNavController.navigateToDrillScreen()
//        }
//    )
//
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun ReportBaseScreen(
//    context: Context,
//    modifier: Modifier = Modifier,
//    reportViewModel: ReportViewModel,
//    backgroundColor: Color = MaterialTheme.colorScheme.background,
//    cardColor: Color = MaterialTheme.colorScheme.secondary,
//    onActionClick: () -> Unit
//) {
//    val headlineStyle = LocalTextStyle.current.copy(
//        fontSize = MaterialTheme.typography.titleLarge.fontSize,
//        color = MaterialTheme.colorScheme.onSecondary,
//        fontWeight = FontWeight.Black
//    )
//    Column(modifier = Modifier.fillMaxSize()) {
//        ScreenHeader(
//            titleResId = R.string.report_title,
//            showAction = true,
//            onActionClick = onActionClick
//        )
//        val state by reportViewModel.activeDurationState.collectAsStateWithLifecycle()
//        when {
//            state.isLoading -> LoadingIndicator()
//            state.records.isEmpty() -> EmptyStateMessage(message = stringResource(R.string.record_is_empty))
//            else -> {
//                LazyColumn(
//                    modifier = modifier
//                        .fillMaxSize()
//                        .background(backgroundColor)
//                        .padding(8.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    item {
//                        ItemsCategory(
//                            headlineResId = R.string.drill_stats,
//                            cardColor = cardColor,
//                            headlineStyle = headlineStyle
//                        ) {
//                            val records = state.records
//                            val pieSlices = records.map {
//                                val color =
//                                    Color(android.graphics.Color.parseColor("#${it.drillColorCode}"))
//                                PieSlice(
//                                    label = it.drillName,
//                                    value = it.totalActiveSeconds.toFloat(),
//                                    color = color,
//                                    totalActiveDuration = it.totalActiveSeconds.seconds.toString()
//                                )
//                            }
//                            PieChart(pieSlices = pieSlices)
//                        }
//                    }
//                    /* item {
//                         ItemsCategory(
//                             headlineResId = R.string.time_stats,
//                             cardColor = cardColor,
//                             headlineStyle = headlineStyle
//                         ) {
//
//                             val barData = listOf<BarData>(
//                                 BarData(
//                                     point = Point(0f, 40f),
//                                     color = Color(0xFF333333),
//                                     label = "0"
//                                 ),
//                                 BarData(
//                                     point = Point(1f, 50f),
//                                     color = Color(0xFF666a86),
//                                     label = "1"
//                                 ),
//                                 BarData(
//                                     point = Point(2f, 95f),
//                                     color = Color(0xFF95B8D1),
//                                     label = "2"
//                                 ),
//                                 BarData(
//                                     point = Point(20f, 60f),
//                                     color = Color(0xFFF53844),
//                                     label = "3"
//                                 )
//                             )
//                             VerticalBarChart(barData)
//                         }
//                     }*/
//                }
//            }
//        }
//    }
//}
//
//
//
//
//
//
//
