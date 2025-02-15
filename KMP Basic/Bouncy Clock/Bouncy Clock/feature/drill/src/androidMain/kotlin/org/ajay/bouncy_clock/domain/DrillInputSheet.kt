package org.ajay.bouncy_clock.domain

import android.os.Parcelable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.parcelize.Parcelize
import org.ajay.bouncy_clock.model.DrillRecordEntity
import org.ajay.bouncy_clock.model.DrillTimerEntity
import org.ajay.bouncy_clock.presentation.Utility.toTitleCase

data class DrillInputSheet(
    val id: Int = 0,
    var name: String? = null,
    var colorCode: String = "ffff824b",
    var duration: String = "30:00",
    var breakDuration: String = "05:00",
    var laps: String = "2"
)

fun DrillInputSheet.toDrillTimerEntity(): DrillTimerEntity {
    return DrillTimerEntity(
        id = this.id,
        title = this.name.toString().toTitleCase(),
        colorCode = this.colorCode,
        duration = this.duration,
        breakDuration = this.breakDuration,
        laps = this.laps
    )
}

fun DrillTimerEntity.toDrillInputSheet(): DrillInputSheet {
    return DrillInputSheet(
        id = this.id,
        name = this.title,
        colorCode = this.colorCode,
        duration = this.duration,
        laps = this.laps,
        breakDuration = this.breakDuration
    )
}



@Parcelize
data class ServiceDrillInput(
    val id:Int = 0,
    val title: String = "Drill",
    val duration: String = "00:00",
    var colorCode: String = "ffff824b",
    val activeDurationInSeconds: Long = 0L,
    val breakDuration: String = "00:00",
    var currentLap: Int = 1,
    val totalLaps: Int = 1
) : Parcelable



fun DrillTimerEntity.toServiceDrillInput(): ServiceDrillInput {
    return ServiceDrillInput(
        id = this.id,
        title = this.title,
        duration = this.duration,
        colorCode = this.colorCode,
        breakDuration = this.breakDuration,
        totalLaps = this.laps.toInt()
    )
}



fun ServiceDrillInput.toDrillRecordEntity(): DrillRecordEntity {
    val localTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val localDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return DrillRecordEntity(
        drillId = this.id,
        name = this.title,
        colorCode = this.colorCode,
        duration = this.duration,
        activeDurationInSeconds = this.activeDurationInSeconds,
        date = localDate,
        startTime = localTime,
        endTime = localTime
    )
}

