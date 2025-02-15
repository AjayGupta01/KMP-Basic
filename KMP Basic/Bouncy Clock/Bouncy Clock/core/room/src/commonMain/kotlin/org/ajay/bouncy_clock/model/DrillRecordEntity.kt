package org.ajay.bouncy_clock.model


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Entity(
    tableName = "drill_record",
    foreignKeys = [
        ForeignKey(
            entity = DrillTimerEntity::class,
            parentColumns = ["id"],  // name in parent column
            childColumns = ["drillId"],    //name in child column
            onDelete = ForeignKey.CASCADE // Delete associated records if the timer is deleted
        )
    ],
    indices = [Index(value = ["drillId"])] // Index for faster lookup by drillId
)
data class DrillRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val recordId: Int = 0, // Unique ID for each record
    val drillId: Int, // Foreign key linking to DrillTimerEntity
    val name: String,
    var colorCode: String,
    val duration: String,
    val activeDurationInSeconds: Long = 0L,
    @TypeConverters(LocalDateConverter::class)
    val date: LocalDate,
    @TypeConverters(LocalTimeConverter::class)
    val startTime: LocalTime,
    @TypeConverters(LocalTimeConverter::class)
    val endTime: LocalTime
)
