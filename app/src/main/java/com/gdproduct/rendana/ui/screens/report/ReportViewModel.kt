package com.gdproduct.rendana.ui.screens.report

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        RendanaDatabase.getDatabase(application)

    private val repository =
        TransactionRepository(database.transactionDao())

    private val _period =
        MutableStateFlow(ReportPeriod.MONTH)

    val period = _period.asStateFlow()

    private val _date =
        MutableStateFlow(LocalDate.now())

    val date = _date.asStateFlow()

    fun setPeriod(period: ReportPeriod) {
        _period.value = period
    }

    fun setDate(date: LocalDate) {
        _date.value = date
    }

    // Semua transaksi pada periode terpilih
    private fun getTransaction() =
        combine(_period, _date) { period, date ->
            getDateRange(period, date)
        }.flatMapLatest { (startDate, endDate) ->
            repository.filterTransaction(
                filter = com.gdproduct.rendana.domain.model.TransactionFilter(
                    startDate = startDate.toString(),
                    endDate = endDate.toString()
                ),
                sort = listOf(
                    com.gdproduct.rendana.domain.model.TransactionSort
                        .DATE_NEWEST_FIRST
                ),
                limit = null
            )
        }

    val transactions = getTransaction().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Total pemasukan
    val totalIncome = transactions.map { list ->
        list.filter {
            it.type.equals("INCOME", ignoreCase = true)
        }.sumOf {
            it.amount
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0L
    )

    // Total pengeluaran
    val totalExpense = transactions.map { list ->
        list.filter {
            it.type.equals("EXPENSE", ignoreCase = true)
        }.sumOf {
            it.amount
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0L
    )
	
	val isCurrentPeriod = combine(
        _period,
        _date
    ) { period, date ->
        val (startDate, endDate) = getDateRange(period, date)
        val today = LocalDate.now()
        today >= startDate && today <= endDate
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        true
    )
    
    // Pemasukan - pengeluaran
    val netBalance = combine(
        totalIncome,
        totalExpense
    ) { income, expense ->
        income - expense
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0L
    )
	
	val previousNetBalance = getPreviousTransactions()
        .map { list ->
            val income = list
                .filter { it.type.equals("INCOME", ignoreCase = true) }
                .sumOf { it.amount }
            
            val expense = list
                .filter { it.type.equals("EXPENSE", ignoreCase = true) }
                .sumOf { it.amount }
            
            income - expense
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0L
        )
	
	fun previousPeriod() {
        _date.value = when (_period.value) {
            ReportPeriod.WEEK -> _date.value.minusWeeks(1)
            ReportPeriod.MONTH -> _date.value.minusMonths(1)
            ReportPeriod.YEAR -> _date.value.minusYears(1)
        }
    }
    
    fun nextPeriod() {
        val currentDate = LocalDate.now()
        val nextDate = when (_period.value) {
            ReportPeriod.WEEK -> _date.value.plusWeeks(1)
            ReportPeriod.MONTH -> _date.value.plusMonths(1)
            ReportPeriod.YEAR -> _date.value.plusYears(1)
        }
        
        val (_, nextEndDate) = getDateRange(_period.value, nextDate)
        if (!nextEndDate.isAfter(currentDate)) {
            _date.value = nextDate
        } else if (getDateRange(_period.value, nextDate).first <= currentDate) {
            _date.value = nextDate
        }
    }
        
    fun goToCurrentPeriod() {
        _date.value = LocalDate.now()
    }

    private fun getDateRange(
        period: ReportPeriod,
        date: LocalDate
    ): Pair<LocalDate, LocalDate> {
        return when (period) {
            ReportPeriod.WEEK -> {
                val start = date.with(
                    TemporalAdjusters.previousOrSame(
                        DayOfWeek.MONDAY
                    )
                )
                start to start.plusDays(6)
            }

            ReportPeriod.MONTH -> {
                date.withDayOfMonth(1) to
                    date.withDayOfMonth(date.lengthOfMonth())
            }

            ReportPeriod.YEAR -> {
                LocalDate.of(date.year, 1, 1) to
                    LocalDate.of(date.year, 12, 31)
            }
        }
    }
	
	private fun getPreviousTransactions() =
    combine(_period, _date) { period, date ->
        val previousDate = when (period) {
            ReportPeriod.WEEK -> date.minusWeeks(1)
            ReportPeriod.MONTH -> date.minusMonths(1)
            ReportPeriod.YEAR -> date.minusYears(1)
        }

        getDateRange(period, previousDate)
    }.flatMapLatest { (startDate, endDate) ->
        repository.filterTransaction(
            filter = com.gdproduct.rendana.domain.model.TransactionFilter(
                startDate = startDate.toString(),
                endDate = endDate.toString()
            ),
            sort = listOf(
                com.gdproduct.rendana.domain.model.TransactionSort.DATE_NEWEST_FIRST
            ),
            limit = null
        )
    }
}