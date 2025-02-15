package org.ajay.bouncy_clock.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.ajay.bouncy_clock.DrillDatabase

actual class GetPlatformDrillDataBase(private val context:Context) {
    actual fun getDatabase(): DrillDatabase {
        val dbFile = context.getDatabasePath("drill.db")
        return Room.databaseBuilder<DrillDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}