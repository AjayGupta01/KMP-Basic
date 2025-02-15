package org.ajay.bouncy_clock.home.model

internal data class CurrentTime(
    val hour: String,
    val minute: String,
    val second: String,
    val amPm: String,
    val date: String,
    val dayName: String
)
