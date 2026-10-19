package com.gdproduct.rendana.data.preferences.profiles

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

private val PROFILE_PICTURE = stringPreferencesKey("profile_picture")

val Context.profilePicture
    get() = profilesDataStore.data.map { preferences ->
        preferences[PROFILE_PICTURE] ?: "Defaults"
    }

suspend fun Context.saveProfilePicture(
    profilePicture: String
) {
    profilesDataStore.edit { preferences ->
        preferences[PROFILE_PICTURE] = profilePicture
    }
}