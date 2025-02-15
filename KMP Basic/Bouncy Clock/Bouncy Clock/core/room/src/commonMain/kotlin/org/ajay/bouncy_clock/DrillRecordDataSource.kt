package org.ajay.bouncy_clock

import org.ajay.bouncy_clock.domain.DatabaseError
import org.ajay.bouncy_clock.domain.EmptyResult
import org.ajay.bouncy_clock.domain.Result
import org.ajay.bouncy_clock.model.DrillActiveDurationSummary
import org.ajay.bouncy_clock.model.DrillRecordEntity
import kotlinx.coroutines.flow.Flow

interface DrillRecordDataSource {
    fun getAllDrillRecords(): Result<Flow<List<DrillRecordEntity>>, DatabaseError>
    suspend fun insertRecordIfNotExists(record: DrillRecordEntity): EmptyResult<DatabaseError>
    suspend fun getDrillLastRecord(drillId: Int): Result<DrillRecordEntity, DatabaseError>
    suspend fun upsertDrillRecord(record: DrillRecordEntity): EmptyResult<DatabaseError>
    fun getDrillActiveDurationSummary(): Result<Flow<List<DrillActiveDurationSummary>>, DatabaseError>
}