package com.gdproduct.rendana.ui.screens.history.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.domain.model.TransactionFilter
import com.gdproduct.rendana.domain.model.TransactionSort
import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.icons.getIcon

import java.time.LocalDate
import java.time.YearMonth
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilterBottomSheet(
    initialFilter: TransactionFilter,
    initialSort: List<TransactionSort>,
    initialLimit: Int,
    categories: List<TransactionCategoryEntity>,
    onDismiss: () -> Unit,
    onApply: (
        TransactionFilter,
        List<TransactionSort>,
        Int
    ) -> Unit
) {
    var merchant by remember(initialFilter) {
        mutableStateOf(initialFilter.merchantName.orEmpty())
    }

    var selectedType by remember(initialFilter) {
        mutableStateOf(initialFilter.type)
    }

    var selectedCategoryId by remember(initialFilter) {
        mutableStateOf(initialFilter.categoryId)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var minAmountText by remember(initialFilter) {
        mutableStateOf(initialFilter.minAmount?.toString().orEmpty())
    }

    var maxAmountText by remember(initialFilter) {
        mutableStateOf(initialFilter.maxAmount?.toString().orEmpty())
    }

    var startDateParts by remember(initialFilter.startDate) {
        mutableStateOf(DateParts.from(initialFilter.startDate))
    }

    var endDateParts by remember(initialFilter.endDate) {
        mutableStateOf(DateParts.from(initialFilter.endDate))
    }

    var limitText by remember(initialLimit) {
        mutableStateOf(initialLimit.toString())
    }

    var selectedSorts by remember(initialSort) {
        mutableStateOf(initialSort.toSet())
    }

    val currentType = selectedType
    val filteredCategories = categories.filter { category ->
        currentType == null || category.type == currentType.name
    }
    
    val selectedCategory = filteredCategories.find {
        it.id == selectedCategoryId
    }
	
	fun selectTransactionType(type: TransactionType?) {
        selectedType = type
        val selectedCategory = categories.find {
            it.id == selectedCategoryId
        }
        
        if (type != null && selectedCategory?.type != type.name) {
            selectedCategoryId = null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Filter transaksi",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = merchant,
                onValueChange = { merchant = it },
                label = { Text("Merchant") },
                placeholder = { Text("Contoh: Indomaret") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Jenis transaksi",
                style = MaterialTheme.typography.titleSmall
            )

            FilterChoice(
                label = "Semua jenis",
                selected = selectedType == null,
                onClick = { selectTransactionType(null) }
            )

            FilterChoice(
                label = "Pengeluaran",
                selected = selectedType == TransactionType.EXPENSE,
                onClick = { selectTransactionType(TransactionType.EXPENSE) }
            )

            FilterChoice(
                label = "Penghasilan",
                selected = selectedType == TransactionType.INCOME,
                onClick = { selectTransactionType(TransactionType.INCOME) }
            )

            HorizontalDivider()

            Text(
                text = "Kategori",
                style = MaterialTheme.typography.titleSmall
            )

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = {
                    categoryExpanded = !categoryExpanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "Semua kategori",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Pilih kategori") },
                    leadingIcon = {
                        androidx.compose.material3.Icon(
                            imageVector = getIcon(
                                selectedCategory?.iconName ?: "category"
                            ),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = categoryExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    },
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = getIcon("category"),
                                    contentDescription = null
                                )
                                Text("Semua kategori")
                            }
                        },
                        onClick = {
                            selectedCategoryId = null
                            categoryExpanded = false
                        }
                    )

                    filteredCategories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = getIcon(category.iconName),
                                        contentDescription = null
                                    )
                                    Text(category.name)
                                }
                            },
                            onClick = {
                                selectedCategoryId = category.id
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "Rentang nominal (rupiah)",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minAmountText,
                    onValueChange = {
                        minAmountText = it.filter(Char::isDigit)
                    },
                    label = { Text("Minimum") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = maxAmountText,
                    onValueChange = {
                        maxAmountText = it.filter(Char::isDigit)
                    },
                    label = { Text("Maksimum") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Rentang tanggal (YYYY-MM-DD)",
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "Rentang tanggal",
                style = MaterialTheme.typography.titleSmall
            )
            
            DateDropdownSelector(
                label = "Tanggal awal",
                value = startDateParts,
                onValueChange = { startDateParts = it }
            )
            
            DateDropdownSelector(
                label = "Tanggal akhir",
                value = endDateParts,
                onValueChange = { endDateParts = it }
            )
            
            HorizontalDivider()

            Text(
                text = "Urutkan berdasarkan",
                style = MaterialTheme.typography.titleSmall
            )

            val sortOptions = listOf(
                TransactionSort.DATE_NEWEST_FIRST to "Tanggal terbaru",
                TransactionSort.DATE_OLDEST_FIRST to "Tanggal terlama",
                TransactionSort.AMOUNT_HIGH_TO_LOW to "Nominal terbesar",
                TransactionSort.AMOUNT_LOW_TO_HIGH to "Nominal terkecil",
                TransactionSort.TYPE_EXPENSE_FIRST to "Pengeluaran dahulu",
                TransactionSort.TYPE_INCOME_FIRST to "Penghasilan dahulu"
            )

            sortOptions.forEach { (sort, label) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sort in selectedSorts,
                        onCheckedChange = { checked ->
                            selectedSorts = if (checked) {
                                selectedSorts + sort
                            } else {
                                selectedSorts - sort
                            }
                        }
                    )
                    Text(label)
                }
            }

            OutlinedTextField(
                value = limitText,
                onValueChange = {
                    limitText = it.filter(Char::isDigit)
                },
                label = { Text("Jumlah transaksi ditampilkan") },
                placeholder = { Text("Contoh: 50") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = {
                        merchant = ""
                        selectedType = null
                        selectedCategoryId = null
                        minAmountText = ""
                        maxAmountText = ""
                        startDateParts = DateParts()
                        endDateParts = DateParts()
                        limitText = "50"
                        selectedSorts = setOf(
                            TransactionSort.DATE_NEWEST_FIRST
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        val newFilter = TransactionFilter(
                            merchantName = merchant.takeIf { it.isNotBlank() },
                            type = selectedType,
                            categoryId = selectedCategoryId,
                            minAmount = minAmountText.toLongOrNull(),
                            maxAmount = maxAmountText.toLongOrNull(),
                            startDate = startDateParts.toDateString(),
                            endDate = endDateParts.toDateString()
                        )

                        onApply(
                            newFilter,
                            selectedSorts.toList().ifEmpty {
                                listOf(
                                    TransactionSort.DATE_NEWEST_FIRST
                                )
                            },
                            (limitText.toIntOrNull() ?: 50)
                                .coerceIn(1, 500)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Terapkan")
                }
            }
        }
    }
}

private data class DateParts(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null
) {
    fun toDateString(): String? {
        val y = year ?: return null
        val m = month ?: return null
        val d = day ?: return null

        return runCatching {
            LocalDate.of(y, m, d).toString()
        }.getOrNull()
    }

    companion object {
        fun from(value: String?): DateParts {
            val date = runCatching {
                value?.let { LocalDate.parse(it) }
            }.getOrNull() ?: return DateParts()

            return DateParts(
                year = date.year,
                month = date.monthValue,
                day = date.dayOfMonth
            )
        }
    }
}

@Composable
private fun FilterChoice(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = { onClick() }
        )
        Text(label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateDropdownSelector(
    label: String,
    value: DateParts,
    onValueChange: (DateParts) -> Unit
) {
    val today = LocalDate.now()

    val years = (today.year downTo 1900).toList()

    val months = if (value.year == null) {
        emptyList()
    } else {
        (1..if (value.year == today.year) {
            today.monthValue
        } else {
            12
        }).toList()
    }

    val days = if (value.year != null && value.month != null) {
        val maxDay = YearMonth.of(
            value.year,
            value.month
        ).lengthOfMonth()

        val allowedMaxDay = if (
            value.year == today.year &&
            value.month == today.monthValue
        ) {
            minOf(maxDay, today.dayOfMonth)
        } else {
            maxDay
        }

        (1..allowedMaxDay).toList()
    } else {
        emptyList()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatePartDropdown(
                label = "Tanggal",
                selected = value.day?.toString(),
                options = days.map { it.toString() },
                enabled = value.month != null,
                modifier = Modifier.weight(0.9f),
                onSelected = { selected ->
                    onValueChange(
                        value.copy(day = selected.toInt())
                    )
                }
            )

            DatePartDropdown(
                label = "Bulan",
                selected = value.month?.let {
                    Month.of(it).getDisplayName(
                        TextStyle.SHORT,
                        Locale("id", "ID")
                    )
                },
                options = months.map {
                    Month.of(it).getDisplayName(
                        TextStyle.SHORT,
                        Locale("id", "ID")
                    )
                },
                enabled = value.year != null,
                modifier = Modifier.weight(1.2f),
                onSelected = { selected ->
                    val month = months.first {
                        Month.of(it).getDisplayName(
                            TextStyle.SHORT,
                            Locale("id", "ID")
                        ) == selected
                    }

                    onValueChange(
                        value.copy(
                            month = month,
                            day = null
                        )
                    )
                }
            )

            DatePartDropdown(
                label = "Tahun",
                selected = value.year?.toString(),
                options = years.map { it.toString() },
                enabled = true,
                modifier = Modifier.weight(1.1f),
                onSelected = { selected ->
                    onValueChange(
                        DateParts(
                            year = selected.toInt(),
                            month = null,
                            day = null
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePartDropdown(
    label: String,
    selected: String?,
    options: List<String>,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected.orEmpty(),
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            singleLine = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 240.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
