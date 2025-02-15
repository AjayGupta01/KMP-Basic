package org.ajay.bouncy_clock.database

import org.ajay.bouncy_clock.DrillDatabase

expect class GetPlatformDrillDataBase{
    fun getDatabase():DrillDatabase
}