package org.ajay.bouncy_clock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import kotlin.collections.filter
import kotlin.collections.plus

class TimerDataStore(private val dataStore: DataStore<Preferences>) {

    val presetTimes: Flow<List<String>> = dataStore.data.map { preferences ->
        val presetsJson = preferences[PreferencesKey.PRESET_TIMES] ?: "[]"
        Json.decodeFromString(presetsJson)// returns list of string
    }

    suspend fun addPresetTime(time: String) {
        try {
            dataStore.edit { preferences ->
                val currentList = Json.decodeFromString<List<String>>(
                    preferences[PreferencesKey.PRESET_TIMES] ?: "[]"
                )
                val updatedList = currentList + time
                preferences[PreferencesKey.PRESET_TIMES] =
                    Json.encodeToString(updatedList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deletePresetTime(name: String) {
        try {
            dataStore.edit { preferences ->
                val currentList = Json.decodeFromString<List<String>>(
                    preferences[PreferencesKey.PRESET_TIMES] ?: "[]"
                )
                val updatedList = currentList.filter {
                    it != name
                }
                preferences[PreferencesKey.PRESET_TIMES] = Json.encodeToString(updatedList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

