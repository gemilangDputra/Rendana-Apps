package com.gdproduct.rendana.ui.screens.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.data.repository.TransactionRepository
import com.gdproduct.rendana.domain.model.TransactionFilter
import com.gdproduct.rendana.domain.model.TransactionSort
import com.gdproduct.rendana.domain.model.TransactionType

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
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalCoroutinesApi::class)
class ReportViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val database = RendanaDatabase.getDatabase(application)
    private val transactionRepository =
        TransactionRepository(database.transactionDao())
    private val categoryRepository =
        TransactionCategoryRepository(database.transactionCategoryDao())
    
    private val _period = MutableStateFlow(ReportPeriod.MONTH)
    val period = _period.asStateFlow()
    private val _date = MutableStateFlow(LocalDate.now())
    val date = _date.asStateFlow()
    
    private val expenseCategoryEntities =
        categoryRepository
            .getByType(TransactionType.EXPENSE.name)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
    
    private val incomeCategoryEntities =
        categoryRepository
            .getByType(TransactionType.INCOME.name)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )
	
    private fun getTransaction() =
        combine(_period, _date) { period, date ->
            getDateRange(period, date)
        }.flatMapLatest { (startDate, endDate) ->
            transactionRepository.filterTransaction(
                filter = TransactionFilter(
                    startDate = startDate.toString(),
                    endDate = endDate.toString()
                ),
                sort = listOf(TransactionSort.DATE_NEWEST_FIRST),
                limit = null
            )
        }
    
    val transactions = getTransaction().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
	
    val expenseCategories = combine(
        transactions,
        expenseCategoryEntities
    ) { transactionList, categoryList ->
        buildAnalysisCategories(
            transactions = transactionList,
            categories = categoryList,
            type = TransactionType.EXPENSE
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
	
    val incomeCategories = combine(
        transactions,
        incomeCategoryEntities
    ) { transactionList, categoryList ->
        buildAnalysisCategories(
            transactions = transactionList,
            categories = categoryList,
            type = TransactionType.INCOME
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
	
    val largestExpenseTransactions = combine(
        transactions,
        expenseCategoryEntities
    ) { transactionList, categoryList ->
        val categoryMap = categoryList.associateBy { it.id }
		
        transactionList
            .filter { it.type.equals("EXPENSE", ignoreCase = true) }
            .sortedByDescending { it.amount }
            .take(10)
            .map { transaction ->
                AnalysisTransaction(
                    id = transaction.id.toLong(),
                    name = transaction.name,
                    categoryName = categoryMap[transaction.categoryId]?.name
                        ?: "Kategori tidak diketahui",
                    date = transaction.date,
                    amount = transaction.amount.toLong()
                )
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
	
    val largestIncomeTransactions = combine(
        transactions,
        incomeCategoryEntities
    ) { transactionList, categoryList ->
        val categoryMap = categoryList.associateBy { it.id }
        
        transactionList
            .filter { it.type.equals("INCOME", ignoreCase = true) }
            .sortedByDescending { it.amount }
            .take(10)
            .map { transaction ->
                AnalysisTransaction(
                    id = transaction.id.toLong(),
                    name = transaction.name,
                    categoryName = categoryMap[transaction.categoryId]?.name
                        ?: "Kategori tidak diketahui",
                    date = transaction.date,
                    amount = transaction.amount.toLong()
                )
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )
	
    val totalIncome = transactions
        .map { list ->
            list
                .filter { it.type.equals("INCOME", ignoreCase = true) }
                .sumOf { it.amount.toLong() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0L
        )
	
    val totalExpense = transactions
        .map { list ->
            list
                .filter { it.type.equals("EXPENSE", ignoreCase = true) }
                .sumOf { it.amount.toLong() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0L
        )
	
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
	
    val chartData = combine(
        transactions,
        _period,
        _date
    ) { transactionList, selectedPeriod, selectedDate ->
        val (startDate, endDate) = getDateRange(
            selectedPeriod,
            selectedDate
        )
        
        val transactionsByDate = transactionList.groupBy {
            LocalDate.parse(it.date)
        }
        
        when (selectedPeriod) {
            ReportPeriod.WEEK,
            ReportPeriod.MONTH -> {
                val numberOfDays = ChronoUnit.DAYS
                    .between(startDate, endDate)
                    .toInt()
                
                (0..numberOfDays).map { offset ->
                    val currentDate = startDate.plusDays(offset.toLong())
                    val dailyTransactions =
                        transactionsByDate[currentDate].orEmpty()
                    
                    ReportChartData(
                        date = currentDate,
                        income = dailyTransactions
                            .filter {
                                it.type.equals("INCOME", ignoreCase = true)
                            }
                            .sumOf { it.amount.toLong() },
                        expense = dailyTransactions
                            .filter {
                                it.type.equals("EXPENSE", ignoreCase = true)
                            }
                            .sumOf { it.amount.toLong() }
                    )
                }
            }
            
            ReportPeriod.YEAR -> {
                val transactionsByMonth = transactionList.groupBy {
                    YearMonth.from(LocalDate.parse(it.date))
                }
                
                (1..12).map { month ->
                    val yearMonth = YearMonth.of(
                        selectedDate.year,
                        month
                    )
                    
                    val monthlyTransactions = transactionsByMonth[yearMonth].orEmpty()
                    
                    ReportChartData(
                        date = yearMonth.atDay(1),
                        income = monthlyTransactions
                            .filter {
                                it.type.equals("INCOME", ignoreCase = true)
                            }
                            .sumOf { it.amount.toLong() },
                        expense = monthlyTransactions
                            .filter {
                                it.type.equals("EXPENSE", ignoreCase = true)
                            }
                            .sumOf { it.amount.toLong() }
                    )
                }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
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
	
    val previousNetBalance = getPreviousTransactions()
        .map { list ->
            val income = list
                .filter { it.type.equals("INCOME", ignoreCase = true) }
                .sumOf { it.amount.toLong() }

            val expense = list
                .filter { it.type.equals("EXPENSE", ignoreCase = true) }
                .sumOf { it.amount.toLong() }

            income - expense
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            0L
        )
	
    fun setPeriod(period: ReportPeriod) {
        _period.value = period
    }
	
    fun setDate(date: LocalDate) {
        _date.value = date
    }
	
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
		
        val (nextStartDate, nextEndDate) =
            getDateRange(_period.value, nextDate)
		
        if (!nextEndDate.isAfter(currentDate) ||
            !nextStartDate.isAfter(currentDate)
        ) {
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
                    TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
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
            transactionRepository.filterTransaction(
                filter = TransactionFilter(
                    startDate = startDate.toString(),
                    endDate = endDate.toString()
                ),
                sort = listOf(TransactionSort.DATE_NEWEST_FIRST),
                limit = null
            )
        }
}