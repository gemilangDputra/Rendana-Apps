package com.gdproduct.rendana.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.LocalContentColor

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun HomeQuickMenu(
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides RendanaThemeColors.iconOnPrimary
            ) {
                icon()
            }
        }
		
        Text(
            text = label,
            color = RendanaThemeColors.textOnPrimary,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}