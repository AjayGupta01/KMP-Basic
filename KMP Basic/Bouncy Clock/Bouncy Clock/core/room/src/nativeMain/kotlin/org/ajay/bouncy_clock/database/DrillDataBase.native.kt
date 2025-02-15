package org.ajay.bouncy_clock.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.ajay.bouncy_clock.DrillDatabase
import platform.Foundation.NSHomeDirectory

actual class GetPlatformDrillDataBase {
    actual fun getDatabase(): DrillDatabase {
        val dbFile = NSHomeDirectory() + "/drill.db"
        return Room.databaseBuilder<DrillDatabase>(
            name = dbFile,
            factory = {
                DrillDatabase::class.instantiateImpl()
            }
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}