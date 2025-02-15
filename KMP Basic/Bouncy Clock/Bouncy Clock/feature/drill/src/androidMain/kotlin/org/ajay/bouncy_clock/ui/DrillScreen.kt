package org.ajay.bouncy_clock.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import org.ajay.bouncy_clock.domain.toServiceDrillInput
import org.ajay.bouncy_clock.service.DrillService
import org.ajay.bouncy_clock.service.DrillServiceUtility
import org.ajay.bouncy_clock.ui.components.AttentionDrillDialog
import org.ajay.bouncy_clock.ui.drill_detail.DrillState
import org.ajay.bouncy_clock.ui.drill_list.DrillListAction
import org.ajay.bouncy_clock.ui.drill_list.DrillListEvent
import org.ajay.bouncy_clock.ui.drill_list.DrillListScreen
import org.ajay.bouncy_clock.ui.drill_list.DrillListViewModel
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.navigation.navigateToDrillDetailScreen
import org.ajay.bouncy_clock.presentation.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun DrillScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    service: DrillService,
    drillListViewModel: DrillListViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val selectedDrill = drillListViewModel.selectedDrill
    val scope = rememberCoroutineScope()
    val currentDrillState = service.currentDrillState
    val isOpenAttentionDialogForStop = drillListViewModel.isOpenAttentionDialogForStop.value
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(snackbarHostState)
    }) { padding ->
        ObserveAsEvents(events = drillListViewModel.events) { event ->
            when (event) {
                is DrillListEvent.Error -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.error.toString())
                    }
                }

                is DrillListEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message.toString())
                    }
                }
            }
        }
        DrillListScreen(
            navController = rootNavController,
            service = service,
            snackbarHostState = snackbarHostState,
            modifier = modifier,
            context = context,
            viewModel = drillListViewModel,
            onAction = { action ->
                drillListViewModel.onAction(action)
                when (action) {
                    is DrillListAction.OnDrillClick -> {
                        val serviceDrillInput = selectedDrill.value.toServiceDrillInput()
                        if (currentDrillState == DrillState.CANCELLED || currentDrillState == DrillState.COMPLETED) {
                            scope.launch {
                                DrillServiceUtility.triggeredForegroundService(
                                    context = context,
                                    action = DrillState.DRILL_RUNNING.name,
                                    drill = serviceDrillInput
                                )
                            }
                            rootNavController.navigateToDrillDetailScreen()
                        } else if (serviceDrillInput.id == service.currentServiceDrillInput.id) {
                            rootNavController.navigateToDrillDetailScreen()
                        }else{
                            drillListViewModel.openAttentionDialogForStop()
                        }

                    }

                    else -> {}
                }
            }
        )

        if (isOpenAttentionDialogForStop) {
            AttentionDrillDialog(
                message = "Attention",
                onDismissRequest = drillListViewModel::closeAttentionDialogForStop,
                onConfirmRequest = {
                    DrillServiceUtility.triggeredForegroundService(
                        context = context,
                        action = DrillState.CANCELLED.name
                    )
                }
            )
        }
    }
}




