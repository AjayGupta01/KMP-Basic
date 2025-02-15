package org.ajay.bouncy_clock

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ajay.bouncy_clock.dao.DrillRecordDao
import com.example.room.drill.dao.DrillTimerDao
import org.ajay.bouncy_clock.model.DrillRecordEntity
import org.ajay.bouncy_clock.model.DrillTimerEntity
import org.ajay.bouncy_clock.model.LocalDateConverter
import org.ajay.bouncy_clock.model.LocalTimeConverter

@Database(
    entities = [DrillTimerEntity::class, DrillRecordEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(LocalDateConverter::class, LocalTimeConverter::class)
abstract class DrillDatabase : RoomDatabase() {
    abstract fun drillTimerDao(): DrillTimerDao
    abstract fun drillRecordDao(): DrillRecordDao
}