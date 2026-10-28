package com.gdproduct.rendana.ui.screens.profile.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdproduct.rendana.data.preferences.settings.saveThemeMode
import com.gdproduct.rendana.data.preferences.settings.themeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val appContext = application.applicationContext
	
    val themeMode: StateFlow<String> =
        appContext.themeMode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "FollowSystem"
        )
	
    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            appContext.saveThemeMode(mode)
        }
    }
}