package org.ajay.bouncy_clock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSFileManager

actual val dataStoreModule = module {
    single {
        CreateDataStore().createDataStore()
    }
}

actual class CreateDataStore() {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(produceFile = {
            val directory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )
            (requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME").toPath()
        })
    }
}