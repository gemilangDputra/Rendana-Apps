package com.gdproduct.rendana.ui.screens.add.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.icons.getIcon
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

val categoryIcons = listOf(
    "category",
    "restaurant",
    "shoppingbag",
    "transportation",
    "store",
    "wallet",
    "payments",
    "paid",
    "accountbalance",
    "savings",
    "receipt",
    "receiptlong",
    "trendingup",
	"trendingflat",
    "trendingdown",
    "redeem",
    "calendarclock",
    "home",
    "person",
    "healthcross",
    "vaccines",
    "school",
    "wifi",
    "electricbolt",
    "add",
    "menu",
    "monitoring",
    "notifications",
	"search",
	"filterlist"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onSaved: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val colors = RendanaThemeColors
	
    var iconSheetVisible by remember {
        mutableStateOf(false)
    }
	
    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }
	
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tambah Kategori",
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary
        )

        // Nama kategori
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Nama kategori",
                style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Contoh: Transportasi")
                },
                singleLine = true
            )
        }
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Tipe kategori",
                style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary
            )
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.type == TransactionType.EXPENSE,
                            onClick = {
                                viewModel.onTypeChange(
                                    TransactionType.EXPENSE
                                )
                            }
                        )
                        
                        Text(
                            text = "Pengeluaran",
                            color = colors.expenseText
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.type == TransactionType.INCOME,
                            onClick = {
                                viewModel.onTypeChange(
                                    TransactionType.INCOME
                                )
                            }
                        )
                        
                        Text(
                            text = "Penghasilan",
                            color = colors.incomeText
                        )
                    }
                }
            }
        }
		
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Ikon kategori",
                style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary
            )
			
            Card(
                onClick = {
                    iconSheetVisible = true
                },
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = getIcon(state.iconName),
                        contentDescription = "Ikon kategori",
                        tint = colors.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Pilih ikon",
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "⌄",
                        color = colors.textSecondary
                    )
                }
            }
        }
		
        if (iconSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    iconSheetVisible = false
                },
                containerColor = colors.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Pilih ikon",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )
					
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categoryIcons) { iconName ->
                            IconButton(
                                onClick = {
                                    viewModel.onIconChange(iconName)
                                    iconSheetVisible = false
                                }
                            ) {
                                Icon(
                                    imageVector = getIcon(iconName),
                                    contentDescription = iconName,
                                    tint = if (state.iconName == iconName) {
                                        colors.primary
                                    } else {
                                        colors.textSecondary
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
		
        state.errorMessage?.let { message ->
            Text(
                text = message,
                color = colors.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
		
        if (state.saved) {
            Text(
                text = "Kategori berhasil disimpan.",
                color = colors.success,
                style = MaterialTheme.typography.bodySmall
            )
        }
		
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = viewModel::saveCategory,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary
            )
        ) {
            Text(
                text = if (state.isSaving) {
                    "Menyimpan..."
                } else {
                    "Simpan Kategori"
                }
            )
        }
    }
}