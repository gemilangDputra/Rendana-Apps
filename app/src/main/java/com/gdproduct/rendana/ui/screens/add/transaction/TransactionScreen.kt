package com.gdproduct.rendana.ui.screens.add.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.ui.icons.getIcon
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

import java.time.LocalDate

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    modifier: Modifier = Modifier,
    onSaved: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val colors = RendanaThemeColors
    val selectedCategory = state.categories.firstOrNull {
        it.id == state.selectedCategoryId
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tambah Transaksi",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
		)
		
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Nama merchant (opsional)",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )
            
            OutlinedTextField(
                value = state.merchantName,
                onValueChange = viewModel::onMerchantNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Contoh: Indomaret, Alfamart")
                },
                singleLine = true
            )
        }
	    
        // Nama transaksi
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Nama transaksi",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Contoh: Makan siang") },
                singleLine = true
            )
        }

        // Nominal transaksi
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Nominal",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("10.000") },
                prefix = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }

        // Jenis transaksi
        Text(
            text = "Jenis transaksi",
            style = MaterialTheme.typography.labelLarge,
            color = colors.textPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TypeOption(
                label = "Pengeluaran",
                selected = state.type == TransactionType.EXPENSE,
                selectedColor = colors.expense,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.onTypeChange(TransactionType.EXPENSE)
            }

            TypeOption(
                label = "Penghasilan",
                selected = state.type == TransactionType.INCOME,
                selectedColor = colors.income,
                modifier = Modifier.weight(1f)
            ) {
                viewModel.onTypeChange(TransactionType.INCOME)
            }
        }

        // Kategori mengikuti jenis transaksi
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Kategori ${if (state.type == TransactionType.EXPENSE) "pengeluaran" else "penghasilan"}",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )

            DropdownField(
                label = "Pilih kategori",
                selectedText = selectedCategory?.name.orEmpty(),
                options = state.categories.map {
                    it.id to it.name
                },
                enabled = state.categories.isNotEmpty(),
                backgroundColor = colors.surface,
                iconName = selectedCategory?.iconName,
                optionIconNames = state.categories.associate {
                    it.id to it.iconName
                },
                onSelected = viewModel::onCategoryChange
            )

            if (state.categories.isEmpty()) {
                Text(
                    text = "Belum ada kategori untuk jenis transaksi ini.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
        }

        // Tanggal
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tanggal transaksi",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentYear = LocalDate.now().year

                DropdownField(
                    label = "Tahun",
                    selectedText = state.year.toString(),
                    options = (1900..currentYear).map {
                        it to it.toString()
                    }.reversed(),
                    modifier = Modifier.weight(1.15f),
                    backgroundColor = colors.surface,
                    onSelected = viewModel::onYearChange
                )

                DropdownField(
                    label = "Bulan",
                    selectedText = state.month?.toString() ?: "—",
                    options = viewModel.availableMonths().map {
                        it to it.toString().padStart(2, '0')
                    },
                    enabled = state.year in 1900..currentYear,
                    modifier = Modifier.weight(0.9f),
                    backgroundColor = colors.surface,
                    onSelected = viewModel::onMonthChange
                )

                DropdownField(
                    label = "Tanggal",
                    selectedText = state.day?.toString() ?: "—",
                    options = viewModel.availableDays().map {
                        it to it.toString().padStart(2, '0')
                    },
                    enabled = state.month != null,
                    modifier = Modifier.weight(0.9f),
                    backgroundColor = colors.surface,
                    onSelected = viewModel::onDayChange
                )
            }

            Text(
                text = "Tanggal tidak boleh melebihi hari ini.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }

        // Note opsional
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Catatan (opsional)",
                style = MaterialTheme.typography.labelLarge,
                color = colors.textPrimary
            )

            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tambahkan catatan...") },
                minLines = 3,
                maxLines = 5
            )
        }

        if (state.error != null) {
            Text(
                text = state.error.orEmpty(),
                color = colors.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (state.isSaved) {
            Text(
                text = "Transaksi berhasil disimpan.",
                color = colors.success,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                viewModel.saveTransaction()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.height(22.dp),
                    color = colors.iconOnPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.padding(start = 8.dp))
                Text("Menyimpan...")
            } else {
                Text("Simpan transaksi")
            }
        }

        if (state.isSaved) {
            TextButton(
                onClick = {
					viewModel.resetForm()
				},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali")
            }
        }
    }
}

@Composable
private fun TypeOption(
    label: String,
    selected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = RendanaThemeColors

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(
            text = label,
            color = if (selected) selectedColor else colors.textPrimary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
private fun DropdownField(
    label: String,
    selectedText: String,
    options: List<Pair<Int, String>>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = RendanaThemeColors.surface,
    iconName: String? = null,
    optionIconNames: Map<Int, String> = emptyMap(),
    onSelected: (Int) -> Unit
) {
    val colors = RendanaThemeColors
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary
        )

        androidx.compose.foundation.layout.Box {
            androidx.compose.material3.OutlinedButton(
                onClick = { expanded = true },
                enabled = enabled && options.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                // Ikon kategori yang sedang dipilih
                if (iconName != null) {
                    androidx.compose.material3.Icon(
                        imageVector = getIcon(iconName),
                        contentDescription = null,
                        tint = colors.textPrimary
                    )
                }

                Text(
                    text = selectedText.ifBlank { "Pilih" },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    color = colors.textPrimary
                )

                Text("▾", color = colors.textSecondary)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.height(280.dp),
				containerColor = backgroundColor
            ) {
                options.forEach { (value, text) ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Ikon masing-masing pilihan
                                optionIconNames[value]?.let { name ->
                                    androidx.compose.material3.Icon(
                                        imageVector = getIcon(name),
                                        contentDescription = null,
                                        tint = colors.textPrimary
                                    )
                                }

                                Text(
                                    text = text,
                                    color = colors.textPrimary
                                )
                            }
                        },
                        onClick = {
                            onSelected(value)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
