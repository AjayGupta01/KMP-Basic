package org.ajay.bouncy_clock.model

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

object LocalDateConverter {
    @TypeConverter
    fun localDateToString(localDate: LocalDate): String {
        return localDate.toString() // Converts to "yyyy-MM-dd"
    }

    @TypeConverter
    fun stringToLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)  // Parse from "yyyy-MM-dd" format
    }
}

