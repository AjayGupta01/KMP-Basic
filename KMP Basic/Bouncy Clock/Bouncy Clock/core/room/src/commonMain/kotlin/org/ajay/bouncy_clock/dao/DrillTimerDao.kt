package com.example.room.drill.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import org.ajay.bouncy_clock.model.DrillTimerEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [DrillTimerEntity] access
 */
@Dao
interface DrillTimerDao {

    /**
     * Fetches a single drill timer entity matching the specified [drillId].
     */
    @Query("SELECT * FROM drill_timer WHERE id = :drillId")
    fun getDrillById(drillId: Int): DrillTimerEntity?

    /**
     * Fetches all drill timer entities.
     */
    @Query("SELECT * FROM drill_timer")
    fun getAllDrills(): Flow<List<DrillTimerEntity>>

    /**
     * Inserts the [drill] if it doesn't exist; otherwise, ignores it.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNotExists(drill: DrillTimerEntity): Long

    /**
     * Inserts the [drill] or updates it if it already exists.
     */
    @Upsert
    suspend fun upsertDrill(drill: DrillTimerEntity)

    /**
     * Deletes the drill timer entity matching the specified [drillId].
     */
    @Query("DELETE FROM drill_timer WHERE id = :drillId")
    suspend fun deleteDrillById(drillId: Int): Int
}