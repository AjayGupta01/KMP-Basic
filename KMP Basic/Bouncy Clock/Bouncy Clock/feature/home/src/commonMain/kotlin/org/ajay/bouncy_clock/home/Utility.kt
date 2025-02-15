package org.ajay.bouncy_clock.home

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import org.ajay.bouncy_clock.home.model.CurrentTime
import org.ajay.bouncy_clock.presentation.Utility.pad

object Utility {

    internal fun LocalDateTime.toFormattedCurrentTime(): CurrentTime {
        return CurrentTime(
            hour = this.hour.pad(),
            minute = this.minute.pad(),
            second = this.second.pad(),
            amPm = this.format(LocalDateTime.Format { amPmMarker(am = "am", pm = "pm") }),
            date = this.format(LocalDateTime.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                chars(" ")
                dayOfMonth()
                chars(", ")
                year()
            }),
            dayName = this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        )
    }
}