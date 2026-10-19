package com.gdproduct.rendana.data.preferences.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

private val THEME_MODE = stringPreferencesKey("theme_mode")

val Context.themeMode
    get() = settingsDataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "FollowSystem"
    }

suspend fun Context.saveThemeMode(mode: String) {
    settingsDataStore.edit { preferences ->
        preferences[THEME_MODE] = mode
    }
}