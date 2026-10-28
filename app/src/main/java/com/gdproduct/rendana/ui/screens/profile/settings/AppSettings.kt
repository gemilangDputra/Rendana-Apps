package com.gdproduct.rendana.ui.screens.profile.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettings(
    currentThemeMode: String,
    onThemeModeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = remember {
        listOf(
            Pair("Mode Terang", "LightMode"),
            Pair("Mode Gelap", "DarkMode"),
            Pair("Ikuti Sistem", "FollowSystem")
        )
    }
	
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = RendanaThemeColors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.titleLarge,
                color = RendanaThemeColors.textPrimary
            )
			
            for ((title, mode) in themeOptions) {
                AppSettingOption(
                    title = title,
                    mode = mode,
                    selectedMode = currentThemeMode,
                    onSelect = onThemeModeSelected
                )
            }
        }
    }
}

@Composable
private fun AppSettingOption(
    title: String,
    mode: String,
    selectedMode: String,
    onSelect: (String) -> Unit
) {
    val isSelected = selectedMode == mode
	
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = { onSelect(mode) }
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null, 
            colors = RadioButtonDefaults.colors(
                selectedColor = RendanaThemeColors.primaryLight,
                unselectedColor = RendanaThemeColors.primaryDark
            )
        )
		
        Text(
            text = title,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            color = RendanaThemeColors.textPrimary
        )
    }
}