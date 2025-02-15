package org.ajay.bouncy_clock.repo

import androidx.sqlite.SQLiteException
import org.ajay.bouncy_clock.DrillRecordDataSource
import org.ajay.bouncy_clock.dao.DrillRecordDao
import org.ajay.bouncy_clock.domain.DatabaseError
import org.ajay.bouncy_clock.domain.EmptyResult
import org.ajay.bouncy_clock.domain.Result
import org.ajay.bouncy_clock.domain.map
import org.ajay.bouncy_clock.domain.safeCall
import org.ajay.bouncy_clock.model.DrillActiveDurationSummary
import org.ajay.bouncy_clock.model.DrillRecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class DrillRecordRepository(private val drillRecordDao: DrillRecordDao) : DrillRecordDataSource {
    override fun getAllDrillRecords(): Result<Flow<List<DrillRecordEntity>>, DatabaseError> {
        return try {
            val result = drillRecordDao.getAllDrillRecords()
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }

    override suspend fun insertRecordIfNotExists(record: DrillRecordEntity): EmptyResult<DatabaseError> {
        return safeCall {
            val rowId = drillRecordDao.insertRecordIfNotExists(record)
            if (rowId == -1L) throw SQLiteException("Record insertion ignored")
        }.map { }
    }

    override suspend fun getDrillLastRecord(drillId: Int): Result<DrillRecordEntity, DatabaseError> {
        return safeCall {
            val record = drillRecordDao.getDrillLastRecord(drillId)
                ?: throw SQLiteException("Record not available")
            record
        }
    }

    override suspend fun upsertDrillRecord(record: DrillRecordEntity): EmptyResult<DatabaseError> {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
        return safeCall {
            val lastRecord = drillRecordDao.getDrillLastRecord(record.drillId)
                ?: throw SQLiteException("Record not available")
            val updatedRecord = lastRecord.copy(endTime = time, activeDurationInSeconds = record.activeDurationInSeconds)
            drillRecordDao.upsertDrillRecord(updatedRecord)
        }.map { }
    }

    override fun getDrillActiveDurationSummary(): Result<Flow<List<DrillActiveDurationSummary>>, DatabaseError> {
        return try {
            val result = drillRecordDao.getDrillActiveDurationSummary()
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }

}