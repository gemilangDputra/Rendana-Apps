package com.gdproduct.rendana.data.preferences.profiles

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.profilesDataStore by preferencesDataStore(
    name = "rendana_profiles"
)