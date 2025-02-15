package org.ajay.bouncy_clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import org.ajay.bouncy_clock.app.App
import org.ajay.bouncy_clock.service.DrillService
import org.ajay.bouncy_clock.service.ServiceManager
import org.ajay.bouncy_clock.service.TimerService

class MainActivity : ComponentActivity() {
    private lateinit var timerManager: ServiceManager<TimerService>
    private lateinit var drillManager: ServiceManager<DrillService>

    override fun onStart() {
        super.onStart()
        timerManager = ServiceManager(this, TimerService::class.java)
        drillManager = ServiceManager(this, DrillService::class.java)
        timerManager.bindService()
        drillManager.bindService()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
            /*val isTimerBound = timerManager.isBound
            val isDrillBound = drillManager.isBound
            val pagerState = rememberPagerState { 3 }
            val navController = rememberNavController()

            GetPermissionOverTiramisu {
                if (isTimerBound && isDrillBound) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = {
                                val title = when (pagerState.currentPage) {
                                    0 -> "Home"
                                    1 -> "Timer"
                                    else -> "Drill"
                                }
                                Text(text = title)
                            },
                                navigationIcon = {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Filled.Whatshot,
                                            contentDescription = null
                                        )
                                    }
                                })
                        }
                    ) { insetPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(insetPadding)
                        ) {

                            HorizontalPager(state = pagerState) {
                                when (it) {
                                    0 -> HomeScreen()
                                    1 -> TimerScreen(timerService = timerManager.getService())
                                    else -> DrillScreen(
                                        rootNavController = navController,
                                        service = drillManager.getService()
                                    )
                                }
                            }
                        }
                    }
                }
            }*/

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerManager.unbindService()
        drillManager.unbindService()
    }
}
