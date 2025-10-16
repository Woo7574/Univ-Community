package com.example.univ_community.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeRepository(context: Context) {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val getThemeMode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.THEME_MODE] ?: "System"
        }

    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit {
            preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode
        }
    }
}
