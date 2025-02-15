package org.ajay.bouncy_clock.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.ajay.bouncy_clock.domain.ActiveDurationState
import org.ajay.bouncy_clock.domain.RecordScreenState
import org.ajay.bouncy_clock.ui.drill_list.DrillListEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.DrillRecordDataSource
import org.ajay.bouncy_clock.domain.onError
import org.ajay.bouncy_clock.domain.onSuccess

class ReportViewModel(private val drillRecordDataSource: DrillRecordDataSource) : ViewModel() {

    private val _recordState = MutableStateFlow(RecordScreenState())
    val recordState = _recordState
        .onStart {
            loadDrillRecords()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            RecordScreenState()
        )
    private val _activeDurationState = MutableStateFlow(ActiveDurationState())
    val activeDurationState = _activeDurationState
        .onStart {
            getDrillActiveDurationSummary()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ActiveDurationState()
        )

    private val _events = Channel<DrillListEvent>()
    val events = _events.receiveAsFlow()


    private fun loadDrillRecords() {
        viewModelScope.launch(Dispatchers.IO) {
            _recordState.update {
                it.copy(
                    isLoading = true
                )
            }
            drillRecordDataSource.getAllDrillRecords()
                .onSuccess { result ->
                    result.collect { records ->
                        _recordState.update {
                            it.copy(
                                isLoading = false,
                                records = records
                            )
                        }
                    }
                }
                .onError { error ->
                    _events.send(DrillListEvent.Error(error))
                }
        }
    }
    private fun getDrillActiveDurationSummary() {
        viewModelScope.launch(Dispatchers.IO) {
            _activeDurationState.update {
                it.copy(
                    isLoading = true
                )
            }
            drillRecordDataSource.getDrillActiveDurationSummary()
                .onSuccess { result ->
                    result.collect { records ->
                        _activeDurationState.update {
                            it.copy(
                                isLoading = false,
                                records = records
                            )
                        }
                    }
                }
                .onError { error ->
                    _events.send(DrillListEvent.Error(error))
                }
        }
    }

}