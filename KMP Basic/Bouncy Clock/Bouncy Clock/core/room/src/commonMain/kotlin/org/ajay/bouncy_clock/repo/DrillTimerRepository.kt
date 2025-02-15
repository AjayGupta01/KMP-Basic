package org.ajay.bouncy_clock.repo


import androidx.sqlite.SQLiteException
import org.ajay.bouncy_clock.DrillTimerDataSource
import com.example.room.drill.dao.DrillTimerDao
import org.ajay.bouncy_clock.domain.DatabaseError
import org.ajay.bouncy_clock.domain.EmptyResult
import org.ajay.bouncy_clock.domain.Result
import org.ajay.bouncy_clock.domain.map
import org.ajay.bouncy_clock.domain.safeCall
import org.ajay.bouncy_clock.model.DrillTimerEntity
import kotlinx.coroutines.flow.Flow

class DrillTimerRepository(private val drillTimerDao: DrillTimerDao) : DrillTimerDataSource {

    override fun getAllDrills(): Result<Flow<List<DrillTimerEntity>>, DatabaseError> {
        return try {
            val result = drillTimerDao.getAllDrills()
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(DatabaseError.Unknown(e))
        }
    }


    override suspend fun insertIfNotExists(drill: DrillTimerEntity): EmptyResult<DatabaseError> {
        return safeCall {
            val rowId = drillTimerDao.insertIfNotExists(drill)
            if (rowId == -1L) throw SQLiteException("Insert ignored")
        }.map { }
    }

    override suspend fun upsertDrill(drill: DrillTimerEntity): EmptyResult<DatabaseError> {
        return safeCall {
            drillTimerDao.upsertDrill(drill)
        }.map { }
    }

    override suspend fun deleteDrillById(drillId: Int): Result<DrillTimerEntity, DatabaseError> {
        return safeCall {
            val deletedDrill = drillTimerDao.getDrillById(drillId)
                ?: throw IllegalStateException("Drill not available with ID: $drillId")

            val rowsDeleted = drillTimerDao.deleteDrillById(drillId)
            if (rowsDeleted == 0) {
                throw IllegalStateException("Failed to delete drill with ID: $drillId")
            }

            deletedDrill
        }
    }


}



