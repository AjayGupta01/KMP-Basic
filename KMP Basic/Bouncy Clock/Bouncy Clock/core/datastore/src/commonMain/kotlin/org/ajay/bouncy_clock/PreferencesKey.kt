package org.ajay.bouncy_clock

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKey {
    val PRESET_TIMES = stringPreferencesKey("preset_times")
    val SELECTED_FONT = stringPreferencesKey("selected_font")
    val SELECTED_THEME = stringPreferencesKey("selected_theme")
    val SCREEN_ON = booleanPreferencesKey("screen_on")
}