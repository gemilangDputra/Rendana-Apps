package com.gdproduct.rendana.ui.screens.history.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.components.TransactionCard
import com.gdproduct.rendana.ui.icons.getIcon
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun TransactionScreen(
    viewModel: TransactionHistoryViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    val context = LocalContext.current
    val categoryRepository = remember(context) {
        TransactionCategoryRepository(
            RendanaDatabase
                .getDatabase(context)
                .transactionCategoryDao()
        )
    }

    val categories by categoryRepository
        .getAll()
        .collectAsState(initial = emptyList())

    if (transactions.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Belum ada transaksi",
                style = MaterialTheme.typography.titleMedium,
                color = RendanaThemeColors.textPrimary
            )

            Text(
                text = "Transaksi yang kamu tambahkan akan muncul di sini.",
                style = MaterialTheme.typography.bodyMedium,
                color = RendanaThemeColors.textSecondary
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = transactions,
                key = { it.id }
            ) { transaction ->
                TransactionHistoryItem(
                    transaction = transaction,
                    categories = categories
                )
            }
        }
    }
}

@Composable
private fun TransactionHistoryItem(
    transaction: TransactionEntity,
    categories: List<TransactionCategoryEntity>
) {
    val category = categories.find {
        it.id == transaction.categoryId
    }

    val type = when (transaction.type) {
        TransactionType.INCOME.name -> TransactionType.INCOME
        else -> TransactionType.EXPENSE
    }

    TransactionCard(
        title = transaction.name,
        amount = transaction.amount,
        type = type,
        category = category?.name ?: "Kategori tidak ditemukan",
        date = transaction.date,
        icon = getIcon(category?.iconName ?: "category"),
        note = transaction.note,
        merchant = transaction.merchantName
    )
}