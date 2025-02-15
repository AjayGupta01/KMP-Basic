package org.ajay.bouncy_clock.model

data class DrillActiveDurationSummary(
    val drillName: String,
    val totalActiveSeconds: Long,
    val drillColorCode: String
)