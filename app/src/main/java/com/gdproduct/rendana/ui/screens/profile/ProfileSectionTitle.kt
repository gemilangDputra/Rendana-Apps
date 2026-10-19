package com.gdproduct.rendana.ui.screens.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        color = RendanaThemeColors.textSecondary
    )
}