package org.ajay.bouncy_clock

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.dsl.module

actual val dataStoreModule = module {
    single{
        CreateDataStore(get()).createDataStore()
    }
}

actual class CreateDataStore(private val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(produceFile = {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath()
        })
    }
}