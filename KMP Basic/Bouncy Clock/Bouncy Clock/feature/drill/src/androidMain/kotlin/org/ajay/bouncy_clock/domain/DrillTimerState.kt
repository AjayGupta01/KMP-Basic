package org.ajay.bouncy_clock.domain

import org.ajay.bouncy_clock.model.DrillTimerEntity


data class DrillTimerState(
    val isLoading: Boolean = false,
    val drills: List<DrillTimerEntity> = emptyList(),
    val selectedDrill: DrillTimerEntity? = null
)
