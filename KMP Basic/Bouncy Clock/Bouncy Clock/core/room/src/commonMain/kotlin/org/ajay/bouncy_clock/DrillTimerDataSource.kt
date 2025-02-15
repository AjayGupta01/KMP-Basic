package org.ajay.bouncy_clock

import org.ajay.bouncy_clock.domain.DatabaseError
import org.ajay.bouncy_clock.domain.EmptyResult
import org.ajay.bouncy_clock.domain.Result
import org.ajay.bouncy_clock.model.DrillTimerEntity
import kotlinx.coroutines.flow.Flow

interface DrillTimerDataSource {
    fun getAllDrills(): Result<Flow<List<DrillTimerEntity>>, DatabaseError>
    suspend fun insertIfNotExists(drill: DrillTimerEntity): EmptyResult<DatabaseError>
    suspend fun upsertDrill(drill: DrillTimerEntity): EmptyResult<DatabaseError>
    suspend fun deleteDrillById(drillId: Int): Result<DrillTimerEntity, DatabaseError>
}