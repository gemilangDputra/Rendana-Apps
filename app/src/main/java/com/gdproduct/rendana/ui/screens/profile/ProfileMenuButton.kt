package com.gdproduct.rendana.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun ProfileMenuButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = RendanaThemeColors.textPrimary
        )
    }
}