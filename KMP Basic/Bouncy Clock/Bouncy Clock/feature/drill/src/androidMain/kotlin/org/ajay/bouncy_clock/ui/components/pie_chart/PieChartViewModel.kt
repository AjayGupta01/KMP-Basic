package org.ajay.bouncy_clock.ui.components.pie_chart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PieChartViewModel : ViewModel() {
    private val _selectedSliceLabel = mutableStateOf("")
    val selectedSliceLabel: State<String> get() = _selectedSliceLabel

    fun selectSlice(label: String) {
        _selectedSliceLabel.value = if (_selectedSliceLabel.value == label) "" else label
    }
}
