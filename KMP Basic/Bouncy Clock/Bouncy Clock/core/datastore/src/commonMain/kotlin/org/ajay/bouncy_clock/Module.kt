package org.ajay.bouncy_clock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module

expect val dataStoreModule: Module

expect class CreateDataStore{
    fun createDataStore(): DataStore<Preferences>
}

internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"