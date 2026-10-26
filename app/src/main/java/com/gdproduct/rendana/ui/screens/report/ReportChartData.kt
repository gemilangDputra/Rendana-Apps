package com.gdproduct.rendana.ui.screens.report

import java.time.LocalDate

data class ReportChartData(
    val date: LocalDate,
    val income: Long,
    val expense: Long
)