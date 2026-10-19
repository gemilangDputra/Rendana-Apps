package com.gdproduct.rendana.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.ui.icons.Menu
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

enum class AddMenu(val title: String) {
    TRANSACTION("Tambah Transaksi"),
    CATEGORY("Tambah Kategori")
}

@Composable
fun AddScreen() {
    var selectedMenu by remember {
        mutableStateOf(AddMenu.TRANSACTION)
    }

    Scaffold(
        containerColor = RendanaThemeColors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AddHeader(
                selectedMenu = selectedMenu,
                onMenuSelected = { selectedMenu = it }
            )

            AddContentContainer(
                modifier = Modifier.padding(16.dp)
            ) {
                when (selectedMenu) {
                    AddMenu.TRANSACTION -> TransactionPlaceholder()
                    AddMenu.CATEGORY -> CategoryPlaceholder()
                }
            }
        }
    }
}

@Composable
fun AddHeader(
    selectedMenu: AddMenu,
    onMenuSelected: (AddMenu) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(RendanaThemeColors.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedMenu.title,
                style = MaterialTheme.typography.titleLarge,
                color = RendanaThemeColors.iconOnPrimary
            )

            Box {
                IconButton(
                    onClick = { expanded = true }
                ) {
                    Icon(
                        imageVector = Menu,
                        contentDescription = "Buka menu",
                        tint = RendanaThemeColors.iconOnPrimary
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = RendanaThemeColors.surface
                ) {
                    AddMenu.entries.forEach { menu ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = menu.title,
                                    color = RendanaThemeColors.textPrimary
                                )
                            },
                            onClick = {
                                onMenuSelected(menu)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddContentContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
    ) {
        content()
    }
}

@Composable
fun TransactionPlaceholder() {
    Placeholder("Form transaksi akan dibuat di sini")
}

@Composable
fun CategoryPlaceholder() {
    Placeholder("Form kategori akan dibuat di sini")
}

@Composable
fun Placeholder(text: String) {
    Text(
        text = text,
        color = RendanaThemeColors.textPrimary
    )
}