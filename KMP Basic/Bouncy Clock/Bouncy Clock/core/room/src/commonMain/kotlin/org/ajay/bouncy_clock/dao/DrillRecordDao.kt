package org.ajay.bouncy_clock.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import org.ajay.bouncy_clock.model.DrillRecordEntity
import org.ajay.bouncy_clock.model.DrillActiveDurationSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface DrillRecordDao {

    @Query("SELECT * FROM drill_record ORDER BY recordId DESC")
    fun getAllDrillRecords(): Flow<List<DrillRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecordIfNotExists(record: DrillRecordEntity): Long

    @Query("SELECT * FROM drill_record WHERE drillId = :drillId ORDER BY recordId DESC LIMIT 1")
    fun getDrillLastRecord(drillId: Int): DrillRecordEntity?

    @Upsert
    suspend fun upsertDrillRecord(record: DrillRecordEntity)

    @Query("SELECT t.title AS drillName, SUM(r.activeDurationInSeconds) AS totalActiveSeconds, t.colorCode AS drillColorCode FROM drill_record r\n" +
            "INNER JOIN drill_timer t ON r.drillId = t.id\n" +
            "GROUP BY r.drillId")
    fun getDrillActiveDurationSummary(): Flow<List<DrillActiveDurationSummary>>
}