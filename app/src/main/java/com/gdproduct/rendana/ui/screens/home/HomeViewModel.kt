package com.gdproduct.rendana.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.repository.TransactionRepository
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.domain.model.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class HomeTransactionUiModel(
    val id: Int,
    val title: String,
    val amount: Long,
    val type: TransactionType,
    val category: String,
    val iconName: String,
    val date: String,
    val merchant: String?,
    val note: String?
)

class HomeViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database =
        RendanaDatabase.getDatabase(application)

    private val transactionRepository =
        TransactionRepository(database.transactionDao())

    private val categoryRepository =
        TransactionCategoryRepository(
            database.transactionCategoryDao()
        )

    val transactions = combine(
        transactionRepository.getAll(),
        categoryRepository.getAll()
    ) { transactions, categories ->

        val categoryMap = categories.associateBy { it.id }

        transactions.map { transaction ->
            val category = categoryMap[transaction.categoryId]

            val type = runCatching {
                TransactionType.valueOf(transaction.type)
            }.getOrDefault(TransactionType.EXPENSE)

            HomeTransactionUiModel(
                id = transaction.id,
                title = transaction.name,
                amount = transaction.amount,
                type = type,
                category = category?.name ?: "Kategori",
                iconName = category?.iconName ?: "category",
                date = formatDate(transaction.date),
                merchant = transaction.merchantName,
                note = transaction.note
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private fun formatDate(value: String): String {
        return try {
            LocalDate.parse(value).format(
                DateTimeFormatter.ofPattern(
                    "d MMM yyyy",
                    Locale("id", "ID")
                )
            )
        } catch (_: Exception) {
            value
        }
    }
}