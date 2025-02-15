package org.ajay.bouncy_clock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.TimerDataStore

class TimerViewModel(private val timerDataStore: TimerDataStore) : ViewModel() {
    val presetTimes: StateFlow<List<String>> = timerDataStore.presetTimes.stateIn(
        viewModelScope,
        SharingStarted.Lazily, emptyList()
    )

    fun addPresetTime(time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            timerDataStore.addPresetTime(time)
        }
    }

    fun deletePresetTimer(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            timerDataStore.deletePresetTime(name)
        }
    }
}

