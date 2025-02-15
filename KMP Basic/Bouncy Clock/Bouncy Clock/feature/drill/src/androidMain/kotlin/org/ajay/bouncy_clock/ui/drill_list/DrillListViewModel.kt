package org.ajay.bouncy_clock.ui.drill_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.ajay.bouncy_clock.domain.DrillInputSheet
import org.ajay.bouncy_clock.domain.DrillTimerState
import org.ajay.bouncy_clock.domain.toDrillInputSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.DrillTimerDataSource
import org.ajay.bouncy_clock.domain.onError
import org.ajay.bouncy_clock.domain.onSuccess
import org.ajay.bouncy_clock.model.DrillTimerEntity

class DrillListViewModel(private val drillTimerDataSource: DrillTimerDataSource) : ViewModel() {
    private val _state = MutableStateFlow(DrillTimerState())
    val state = _state
        .onStart {
            loadDrills()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DrillTimerState()
        )

    private val _selectedDrill = mutableStateOf(DrillTimerEntity())
    val selectedDrill: State<DrillTimerEntity> = _selectedDrill

    private val _events = Channel<DrillListEvent>()
    val events = _events.receiveAsFlow()

    private val _drillInput = mutableStateOf(DrillInputSheet())
    val drillInput: State<DrillInputSheet> = _drillInput

    var expandedDrillId = mutableStateOf<Int?>(null)
        private set

    private val _isOpenInputBottomSheet = mutableStateOf(false)
    val isOpenInputBottomSheet: State<Boolean> = _isOpenInputBottomSheet

    private val _isOpenAttentionDialogForStop = mutableStateOf(false)
    val isOpenAttentionDialogForStop: State<Boolean> = _isOpenAttentionDialogForStop

    private val _isOpenAttentionDialogForDelete = mutableStateOf(false)
    val isOpenAttentionDialogForDelete: State<Boolean> = _isOpenAttentionDialogForDelete

    private val _deletedDrill = mutableStateOf(DrillTimerEntity())
    val deletedDrill: State<DrillTimerEntity> = _deletedDrill

    fun onAction(action: DrillListAction) {
        when (action) {
            is DrillListAction.OnDrillClick -> {
                selectDrill(action.drill)
                collapseDrill()
            }

            is DrillListAction.OnDeleteClick -> {
                deleteDrillById(action.drill.id)
            }

            is DrillListAction.OnUpdateClick -> {
                openInputBottomSheetForUpdate(action.drill)
            }
        }
    }

    private fun selectDrill(drill: DrillTimerEntity) {
        _state.update { it.copy(selectedDrill = drill) }
        _selectedDrill.value = drill
    }

    private fun loadDrills() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            drillTimerDataSource.getAllDrills()
                .onSuccess { result ->
                    result.collect { drills ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                drills = drills
                            )
                        }
                    }
                }
                .onError { error ->
                    _events.send(DrillListEvent.Error(error))
                }
        }
    }



    fun addOrUpdateDrill(drill: DrillTimerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (drill.id == 0) {
                // Insert new drill
                drillTimerDataSource.insertIfNotExists(drill)
                    .onError { error -> _events.send(DrillListEvent.Error(error)) }
            } else {
                // Update existing drill
                drillTimerDataSource.upsertDrill(drill)
                    .onError { error -> _events.send(DrillListEvent.Error(error)) }
            }
        }
    }



    fun deleteDrillById(drillId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            drillTimerDataSource.deleteDrillById(drillId).onSuccess {
                _events.send(DrillListEvent.Success("Deleted ${it.title}"))
            }.onError { error ->
                _events.send(DrillListEvent.Error(error))
            }
        }
    }



    // Expand a specific drill
    fun expandDrill(drillId: Int) {
        expandedDrillId.value = drillId
    }

    // Collapse all drill
    fun collapseDrill() {
        expandedDrillId.value = null
    }


    fun openInputBottomSheetForInsert() {
        _drillInput.value = DrillInputSheet()   //reset the previous value
        _isOpenInputBottomSheet.value = true
    }

    fun openInputBottomSheetForUpdate(drill: DrillTimerEntity) {
        _drillInput.value = drill.toDrillInputSheet()
        _isOpenInputBottomSheet.value = true
    }

    fun closeInputBottomSheet() {
        _isOpenInputBottomSheet.value = false
    }

    fun openAttentionDialogForStop(){
        _isOpenAttentionDialogForStop.value = true
    }

    fun closeAttentionDialogForStop(){
        _isOpenAttentionDialogForStop.value = false
    }

    fun openAttentionDialogForDelete(drill: DrillTimerEntity){
        _deletedDrill.value = drill
        _isOpenAttentionDialogForDelete.value = true
    }

    fun closeAttentionDialogForDelete(){
        _isOpenAttentionDialogForDelete.value = false
    }
}




