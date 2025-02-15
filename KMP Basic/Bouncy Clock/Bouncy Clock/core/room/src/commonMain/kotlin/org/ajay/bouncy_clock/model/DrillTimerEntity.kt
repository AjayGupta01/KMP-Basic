package org.ajay.bouncy_clock.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "drill_timer"
)
data class DrillTimerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    var colorCode: String = "ffff824b",
    val duration: String = "30:00",
    val laps: String = "2",
    val breakDuration: String = "05:00"
)
