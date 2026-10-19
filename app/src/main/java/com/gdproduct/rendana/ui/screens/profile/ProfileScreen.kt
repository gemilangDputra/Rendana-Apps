package com.gdproduct.rendana.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.gdproduct.rendana.ui.screens.profile.settings.ProfileSettings
import com.gdproduct.rendana.ui.screens.profile.settings.ProfileSettingsViewModel
import com.gdproduct.rendana.ui.screens.settings.AppSettings
import com.gdproduct.rendana.ui.screens.settings.SettingsViewModel

@Composable
fun ProfileScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    profileSettingsViewModel: ProfileSettingsViewModel = viewModel()
) {
    var showAppSettings by remember { mutableStateOf(false) }
    var showProfileSettings by remember { mutableStateOf(false) }
    var showAboutApp by remember { mutableStateOf(false) }

    val themeMode by settingsViewModel.themeMode.collectAsState()
    val nickname by profileSettingsViewModel.nickname.collectAsState()
    val profilePicture by profileSettingsViewModel.profilePicture.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header berada paling atas dan tidak terkena padding konten.
        ProfileHeader(
            nickname = nickname,
            profilePicture = profilePicture
        )

        // Padding hanya berlaku untuk bagian menu.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileSectionTitle(title = "Akun")

                ProfileMenuButton(
                    text = "Pengaturan Profil",
                    onClick = { showProfileSettings = true }
                )

                ProfileMenuButton(
                    text = "Pengaturan Keamanan",
                    onClick = {}
                )
            }

            HorizontalDivider()

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileSectionTitle(title = "Preferensi")

                ProfileMenuButton(
                    text = "Pengaturan Aplikasi",
                    onClick = { showAppSettings = true }
                )
            }

            HorizontalDivider()

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileSectionTitle(title = "Privasi")

                ProfileMenuButton(
                    text = "Tempat Sampah",
                    onClick = {}
                )
            }

            HorizontalDivider()

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileSectionTitle(title = "Lainnya")

                ProfileMenuButton(
                    text = "Tentang Aplikasi",
                    onClick = { showAboutApp = true }
                )
            }
        }
    }

    if (showProfileSettings) {
        ProfileSettings(
            onDismiss = { showProfileSettings = false },
            viewModel = profileSettingsViewModel
        )
    }

    if (showAppSettings) {
        AppSettings(
            currentThemeMode = themeMode,
            onThemeModeSelected = { mode ->
                settingsViewModel.setThemeMode(mode)
            },
            onDismiss = { showAppSettings = false }
        )
    }

    if (showAboutApp) {
        AboutAppBottomSheet(
            onDismiss = { showAboutApp = false }
        )
    }
}