package com.gdproduct.rendana.ui.screens.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import com.gdproduct.rendana.ui.icons.Monitoring
import com.gdproduct.rendana.ui.theme.RendanaThemeColors
import com.gdproduct.rendana.utils.AmountFormat
import com.gdproduct.rendana.utils.formatAmount

import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

@Composable
fun ReportScreen(
    viewModel: ReportViewModel = viewModel()
) {
    val period by viewModel.period.collectAsStateWithLifecycle()
    val date by viewModel.date.collectAsStateWithLifecycle()
    val isCurrentPeriod by viewModel.isCurrentPeriod.collectAsStateWithLifecycle()
    val totalIncome by viewModel.totalIncome.collectAsStateWithLifecycle()
    val totalExpense by viewModel.totalExpense.collectAsStateWithLifecycle()
    val netBalance by viewModel.netBalance.collectAsStateWithLifecycle()
    val previousNetBalance by viewModel.previousNetBalance.collectAsStateWithLifecycle()
    val chartData by viewModel.chartData.collectAsStateWithLifecycle()
    val expenseCategories by viewModel.expenseCategories.collectAsStateWithLifecycle()
    val incomeCategories by viewModel.incomeCategories.collectAsStateWithLifecycle()
    val largestExpenseTransactions by
        viewModel.largestExpenseTransactions.collectAsStateWithLifecycle()
    val largestIncomeTransactions by
        viewModel.largestIncomeTransactions.collectAsStateWithLifecycle()
    
    val periodLabel = when (period) {
        ReportPeriod.WEEK -> {
            val start = date.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            )
            val end = start.plusDays(6)
			
            "${start.format(DateTimeFormatter.ofPattern("d MMM", Locale("id", "ID")))} - " +
                end.format(
                    DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID"))
                )
        }
		
        ReportPeriod.MONTH ->
            date.format(
                DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID"))
            )
        
        ReportPeriod.YEAR -> date.year.toString()
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Laporan",
                    style = MaterialTheme.typography.titleLarge,
                    color = RendanaThemeColors.textPrimary
                )
				
                Text(
                    text = "Ringkasan Keuanganmu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RendanaThemeColors.textSecondary
                )
            }
			
            Icon(
                imageVector = Monitoring,
                contentDescription = "Laporan",
                tint = RendanaThemeColors.primary
            )
        }
		
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.previousPeriod() }
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Periode sebelumnya"
                )
            }
			
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = periodLabel,
                    style = MaterialTheme.typography.titleMedium
                )
				
                if (!isCurrentPeriod) {
                    TextButton(
                        onClick = { viewModel.goToCurrentPeriod() }
                    ) {
                        Text("Kembali ke sekarang")
                    }
                }
            }
			
            IconButton(
                onClick = { viewModel.nextPeriod() },
                enabled = !isCurrentPeriod
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Periode berikutnya"
                )
            }
        }
		
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReportPeriod.entries.forEach { item ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = period == item,
                    onClick = { viewModel.setPeriod(item) },
                    label = {
                        Text(
                            text = when (item) {
                                ReportPeriod.WEEK -> "Minggu"
                                ReportPeriod.MONTH -> "Bulan"
                                ReportPeriod.YEAR -> "Tahun"
                            }
                        )
                    }
                )
            }
        }
		
        ReportSummaryCard(
            netBalance = netBalance,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            previousNetBalance = previousNetBalance
        )
		
        ReportChartCard(
            data = chartData,
            chartIncome = RendanaThemeColors.chartIncome,
            chartExpense = RendanaThemeColors.chartExpense,
            chartGrid = RendanaThemeColors.chartGrid,
            chartAxis = RendanaThemeColors.chartAxis,
            chartTooltipBackground = RendanaThemeColors.chartTooltipBackground,
            chartTooltipText = RendanaThemeColors.chartTooltipText
        )
		
        CategoryAnalysisCard(
            title = "Analisis Pengeluaran",
            totalAmount = totalExpense,
            categories = expenseCategories,
            transactions = largestExpenseTransactions,
            formatAmount = { amount ->
                formatAmount(amount, AmountFormat.COMPACT)
            }
        )
        
        CategoryAnalysisCard(
            title = "Analisis Pemasukan",
            totalAmount = totalIncome,
            categories = incomeCategories,
            transactions = largestIncomeTransactions,
            formatAmount = { amount ->
                formatAmount(amount, AmountFormat.COMPACT)
            }
        )
    }
}