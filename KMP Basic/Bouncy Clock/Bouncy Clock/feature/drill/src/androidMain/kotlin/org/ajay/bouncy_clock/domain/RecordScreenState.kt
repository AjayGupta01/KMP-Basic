package org.ajay.bouncy_clock.domain

import org.ajay.bouncy_clock.model.DrillActiveDurationSummary
import org.ajay.bouncy_clock.model.DrillRecordEntity


data class RecordScreenState(
    val isLoading: Boolean = false,
    val records: List<DrillRecordEntity> = emptyList(),
)

data class ActiveDurationState(
    val isLoading: Boolean = false,
    val records: List<DrillActiveDurationSummary> = emptyList(),
)
