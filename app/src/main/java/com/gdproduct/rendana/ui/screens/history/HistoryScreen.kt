package com.gdproduct.rendana.ui.screens.history

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.repository.TransactionRepository
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.domain.model.TransactionFilter
import com.gdproduct.rendana.domain.model.TransactionSort
import com.gdproduct.rendana.ui.icons.Menu
import com.gdproduct.rendana.ui.icons.Search
import com.gdproduct.rendana.ui.icons.FilterList
import com.gdproduct.rendana.ui.screens.history.transaction.TransactionScreen
import com.gdproduct.rendana.ui.screens.history.transaction.TransactionHistoryViewModel
import com.gdproduct.rendana.ui.screens.history.transaction.TransactionFilterBottomSheet
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

enum class HistoryType(val title: String) {
    TRANSACTION("Riwayat Transaksi"),
    REMINDER("Riwayat Pengingat"),
    SAVING_TARGET("Riwayat Target Tabungan")
}

@Composable
fun HistoryScreen() {
    var selectedHistory by remember {
        mutableStateOf(HistoryType.TRANSACTION)
    }
    
    var searchQuery by remember {
        mutableStateOf("")
    }
    
    var filterSheetVisible by remember {
        mutableStateOf(false)
    }
    
    var transactionFilter by remember {
        mutableStateOf(TransactionFilter())
    }
    
    var transactionSort by remember {
        mutableStateOf(
            listOf(TransactionSort.DATE_NEWEST_FIRST)
        )
    }
    
    var transactionLimit by remember {
        mutableStateOf(50)
    }
    
    val context = LocalContext.current
    val categoryRepository = remember(context) {
        TransactionCategoryRepository(
            RendanaDatabase.getDatabase(context).transactionCategoryDao()
        )
    }
    
    val categories by categoryRepository
        .getAll()
        .collectAsState(initial = emptyList())
    
    val database = remember {
        RendanaDatabase.getDatabase(context)
    }
    
    val transactionHistoryViewModel: TransactionHistoryViewModel =
        viewModel(
            factory = remember(database) {
                object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(
                        modelClass: Class<T>
                    ): T {
                        if (
                            modelClass.isAssignableFrom(
                                TransactionHistoryViewModel::class.java
                            )
                        ) {
                            return TransactionHistoryViewModel(
                                repository = TransactionRepository(
                                    database.transactionDao()
                                )
                            ) as T
                        }
                        
                        throw IllegalArgumentException(
                            "Unknown ViewModel: ${modelClass.name}"
                        )
                    }
                }
            }
        )
    
    LaunchedEffect(searchQuery, selectedHistory) {
        if (selectedHistory == HistoryType.TRANSACTION) {
            transactionHistoryViewModel.updateSearchQuery(searchQuery)
        }
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
            HistoryHeader(
                selectedHistory = selectedHistory,
                searchQuery = searchQuery,
                onHistorySelected = { history ->
                    if (selectedHistory != history) {
                        selectedHistory = history
                        searchQuery = ""
                    }
                },
                onSearchChange = {
                    searchQuery = it
                },
                onFilterClick = {
                    if (selectedHistory == HistoryType.TRANSACTION) {
                        filterSheetVisible = true
                    }
                }
            )
            
            HistoryContentContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (selectedHistory) {
                    HistoryType.TRANSACTION -> {
                        TransactionScreen(
                            viewModel = transactionHistoryViewModel
                        )
                    }
                    
                    HistoryType.REMINDER -> {
                        PlaceholderHistory(
                            title = "Riwayat pengingat",
                            description =
                                "Riwayat pengingat akan tersedia di sini."
                        )
                    }
                    
                    HistoryType.SAVING_TARGET -> {
                        PlaceholderHistory(
                            title = "Riwayat target tabungan",
                            description =
                                "Riwayat target tabungan akan tersedia di sini."
                        )
                    }
                }
            }
        }
    }
    
    if (
        filterSheetVisible &&
        selectedHistory == HistoryType.TRANSACTION
    ) {
        TransactionFilterBottomSheet(
            initialFilter = transactionFilter,
            initialSort = transactionSort,
            initialLimit = transactionLimit,
			categories = categories,
            onDismiss = {
                filterSheetVisible = false
            },
            onApply = { newFilter, newSort, newLimit ->
                transactionFilter = newFilter
                transactionSort = newSort
                transactionLimit = newLimit

                transactionHistoryViewModel.updateFilter(newFilter)
                transactionHistoryViewModel.updateSort(newSort)
                transactionHistoryViewModel.updateLimit(newLimit)

                filterSheetVisible = false
            }
        )
    }
}

@Composable
private fun HistoryHeader(
    selectedHistory: HistoryType,
    searchQuery: String,
    onHistorySelected: (HistoryType) -> Unit,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    var historyMenuExpanded by remember {
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
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box {
                IconButton(
                    onClick = {
                        historyMenuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Menu,
                        contentDescription = "Pilih jenis riwayat",
                        tint = RendanaThemeColors.iconOnPrimary
                    )
                }
                
                DropdownMenu(
                    expanded = historyMenuExpanded,
                    onDismissRequest = {
                        historyMenuExpanded = false
                    },
                    containerColor = RendanaThemeColors.surface
                ) {
                    HistoryType.entries.forEach { history ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = history.title,
                                    color = RendanaThemeColors.textPrimary
                                )
                            },
                            onClick = {
                                onHistorySelected(history)
                                historyMenuExpanded = false
                            }
                        )
                    }
                }
            }
            
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RendanaThemeColors.surface)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Search,
                    contentDescription = "Cari",
                    tint = RendanaThemeColors.textSecondary
                )
                
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = when (selectedHistory) {
                                HistoryType.TRANSACTION ->
                                    "Cari transaksi..."

                                HistoryType.REMINDER ->
                                    "Cari pengingat..."

                                HistoryType.SAVING_TARGET ->
                                    "Cari target..."
                            },
                            color = RendanaThemeColors.textSecondary,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }

                    BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = RendanaThemeColors.textPrimary
                        ),
                        cursorBrush = SolidColor(
                            RendanaThemeColors.primary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            IconButton(
                onClick = onFilterClick
            ) {
                Icon(
                    imageVector = FilterList,
                    contentDescription = "Filter riwayat",
                    tint = RendanaThemeColors.iconOnPrimary
                )
            }
        }
        
        Text(
            text = selectedHistory.title,
            style = MaterialTheme.typography.titleLarge,
            color = RendanaThemeColors.textOnPrimary,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 16.dp
            )
        )
    }
}

@Composable
private fun PlaceholderHistory(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = RendanaThemeColors.textPrimary
        )
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = RendanaThemeColors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HistoryContentContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = 600.dp)
    ) {
        content()
    }
}
