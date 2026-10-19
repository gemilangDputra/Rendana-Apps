package com.gdproduct.rendana.ui.screens.profile.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdproduct.rendana.data.preferences.profiles.nickname
import com.gdproduct.rendana.data.preferences.profiles.profilePicture
import com.gdproduct.rendana.data.preferences.profiles.saveNickname
import com.gdproduct.rendana.data.preferences.profiles.saveProfilePicture
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val appContext = application.applicationContext
	
    val nickname: StateFlow<String> =
        appContext.nickname.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "User"
        )
    
    val profilePicture: StateFlow<String> =
        appContext.profilePicture.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "Defaults"
        )
    
    fun saveProfileChanges(
        nickname: String,
        profilePicture: String
    ) {
        viewModelScope.launch {
            appContext.saveNickname(nickname.trim())
            appContext.saveProfilePicture(profilePicture)
        }
    }
}