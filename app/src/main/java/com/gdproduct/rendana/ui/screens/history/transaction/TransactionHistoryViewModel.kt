package com.gdproduct.rendana.ui.screens.history.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.gdproduct.rendana.data.repository.TransactionRepository
import com.gdproduct.rendana.domain.model.TransactionFilter
import com.gdproduct.rendana.domain.model.TransactionSort
import com.gdproduct.rendana.data.local.entity.TransactionEntity

import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionHistoryViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val filter = MutableStateFlow(TransactionFilter())

    private val sort = MutableStateFlow(
        listOf(TransactionSort.DATE_NEWEST_FIRST)
    )

    private val searchQuery = MutableStateFlow("")

    private val limit = MutableStateFlow(50)

    val transactions: StateFlow<List<TransactionEntity>> =
        combine(filter, sort, searchQuery, limit) {
                currentFilter, currentSort, query, currentLimit ->
            HistoryQuery(
                filter = currentFilter,
                sort = currentSort,
                query = query,
                limit = currentLimit
            )
        }.flatMapLatest { params ->
            repository.filterTransaction(
                filter = params.filter,
                sort = params.sort,
                limit = params.limit,
                query = params.query
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun updateFilter(newFilter: TransactionFilter) {
        filter.value = newFilter
    }

    fun updateSort(newSort: List<TransactionSort>) {
        sort.value = newSort
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun updateLimit(newLimit: Int) {
        limit.value = newLimit.coerceIn(1, 500)
    }

    private data class HistoryQuery(
        val filter: TransactionFilter,
        val sort: List<TransactionSort>,
        val query: String,
        val limit: Int
    )
}
