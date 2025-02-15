package org.ajay.bouncy_clock.ui.drill_list

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.ajay.bouncy_clock.domain.ServiceDrillInput
import org.ajay.bouncy_clock.service.DrillService
import org.ajay.bouncy_clock.ui.components.ActionIcon
import org.ajay.bouncy_clock.ui.components.AttentionDrillDialog
import org.ajay.bouncy_clock.ui.components.EmptyStateMessage
import org.ajay.bouncy_clock.ui.components.InputBottomSheet
import org.ajay.bouncy_clock.ui.components.LoadingIndicator
import org.ajay.bouncy_clock.ui.components.SwipeableItemWithActions
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.feature.drill.R
import org.ajay.bouncy_clock.model.DrillTimerEntity
import org.ajay.bouncy_clock.navigation.navigateToReportScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DrillListScreen(
    context: Context,
    snackbarHostState: SnackbarHostState,
    service: DrillService,
    viewModel: DrillListViewModel,
    onAction: (DrillListAction) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOpenInputBottomSheet by viewModel.isOpenInputBottomSheet
    val drillInput by viewModel.drillInput
    val runningDrill = service.currentServiceDrillInput

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(onClick = {
                    navController.navigateToReportScreen()
                }) {
                    Icon(imageVector = Icons.Filled.BarChart, contentDescription = null)
                }
                ExtendedFloatingActionButton(
                    onClick = viewModel::openInputBottomSheetForInsert,
                    text = {
                        Text("New Drill")
                    },
                    icon = {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    }
                )
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingIndicator()
            state.drills.isNotEmpty() -> DrillListContent(
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                runningDrill = runningDrill,
                onAction = onAction,
                viewModel = viewModel,
                drills = state.drills,
            )

            else -> EmptyStateMessage()
        }
    }

    if (isOpenInputBottomSheet) {
        InputBottomSheet(
            context = context,
            onDismissRequest = viewModel::closeInputBottomSheet,
            drillInput = drillInput,
            onAddOrUpdateDrillClick = { drill ->
                viewModel.addOrUpdateDrill(drill)
                viewModel.closeInputBottomSheet()
            }
        )
    }
}


@Composable
private fun DrillListContent(
    snackbarHostState: SnackbarHostState,
    viewModel: DrillListViewModel,
    runningDrill: ServiceDrillInput,
    drills: List<DrillTimerEntity>,
    innerPadding: PaddingValues,
    onAction: (DrillListAction) -> Unit
) {
    val isOpenAttentionDialogForDelete = viewModel.isOpenAttentionDialogForDelete.value
    var deletedDrill = viewModel.deletedDrill.value
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = drills,
            key = { drill -> drill.id }
        ) { drill ->
            val isRevealed = viewModel.expandedDrillId.value == drill.id
            SwipeableItemWithActions(
                isRevealed = isRevealed,
                actions = {
                    ActionIcon(
                        icon = Icons.Filled.Edit,
                        backgroundColor = Color(0xFF525254),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        viewModel.collapseDrill()
                        onAction(DrillListAction.OnUpdateClick(drill))
                    }
                    ActionIcon(
                        icon = Icons.Filled.Delete,
                        backgroundColor = Color(0xFF69696A),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        viewModel.collapseDrill()
                        viewModel.openAttentionDialogForDelete(drill)
                    }
                },
                onExpanded = {
                    viewModel.expandDrill(drill.id)
                },
                onCollapsed = {
                    viewModel.collapseDrill()
                }
            ) {
                DrillListItem(
                    drill = drill,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onAction(DrillListAction.OnDrillClick(drill))
                    }
                )
            }
        }
    }
    if (isOpenAttentionDialogForDelete) {
        AttentionDrillDialog(
            message = stringResource(R.string.delete_drill_msg),
            confirmButtonTitle = "Delete",
            onDismissRequest = viewModel::closeAttentionDialogForDelete
        ) {
            if (runningDrill.id == deletedDrill.id) {
                scope.launch{
                    snackbarHostState.showSnackbar("Can't delete, ${deletedDrill.title} is running")
                }
            } else {
                onAction(DrillListAction.OnDeleteClick(deletedDrill))
            }
        }
    }
}
