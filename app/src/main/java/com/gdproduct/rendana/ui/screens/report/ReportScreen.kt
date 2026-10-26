package com.gdproduct.rendana.ui.screens.report

import androidx.lifecycle.viewmodel.compose.viewModel

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

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.gdproduct.rendana.ui.icons.Monitoring
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    val periodLabel = when (period) {
        ReportPeriod.WEEK -> {
            val start = date.with(
                java.time.temporal.TemporalAdjusters.previousOrSame(
                    java.time.DayOfWeek.MONDAY
                )
            )
            val end = start.plusDays(6)
            "${start.format(DateTimeFormatter.ofPattern("d MMM", Locale("id", "ID")))} - " +
                end.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("id", "ID")))
        }

        ReportPeriod.MONTH ->
            date.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID")))

        ReportPeriod.YEAR -> date.year.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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

        // Pemilih periode
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

        // Ringkasan keuangan
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
    }
}