package org.ajay.bouncy_clock.model

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

object LocalTimeConverter {

    @TypeConverter
    fun localTimeToString(localTime: LocalTime): String {
        return localTime.toString() // Converts to "HH:mm:ss"
    }

    @TypeConverter
    fun stringToLocalTime(timeString: String): LocalTime {
        return LocalTime.parse(timeString)  // Parse from "HH:mm:ss" format
    }
}

