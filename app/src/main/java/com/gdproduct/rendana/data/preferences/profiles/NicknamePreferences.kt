package com.gdproduct.rendana.data.preferences.profiles

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

private val NICKNAME = stringPreferencesKey("nickname")

val Context.nickname
    get() = profilesDataStore.data.map { preferences ->
        preferences[NICKNAME] ?: "User"
    }

suspend fun Context.saveNickname(nickname: String) {
    profilesDataStore.edit { preferences ->
        preferences[NICKNAME] = nickname
    }
}