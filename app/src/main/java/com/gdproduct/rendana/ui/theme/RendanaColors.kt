package com.gdproduct.rendana.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class RendanaColors(
    // Primary
    val primary: Color,
    val primaryDark: Color,
    val primaryLight: Color,
    val primarySoft: Color,
    val primaryContrast: Color,

    // Material-style colors
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val background: Color,
    val backgroundSecondary: Color,
    val surface: Color,
    val surfaceSoft: Color,
    val surfaceElevated: Color,
    val surfaceVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val onPrimary: Color,
    val outline: Color,
    val outlineVariant: Color,
    val scrim: Color,

    // Income
    val income: Color,
    val incomeDark: Color,
    val incomeLight: Color,
    val incomeSoft: Color,
    val incomeBackground: Color,
    val incomeText: Color,

    // Expense
    val expense: Color,
    val expenseDark: Color,
    val expenseLight: Color,
    val expenseSoft: Color,
    val expenseBackground: Color,
    val expenseText: Color,

    // Balance and summary cards
    val balanceCard: Color,
    val balanceText: Color,
    val balanceTextSecondary: Color,
    val summaryIncomeCard: Color,
    val summaryExpenseCard: Color,

    // Reminder
    val reminder: Color,
    val reminderDark: Color,
    val reminderSoft: Color,
    val reminderBackground: Color,

    // Charts
    val chartIncome: Color,
    val chartExpense: Color,
    val chartBalance: Color,
    val chartSavings: Color,
    val chartGrid: Color,
    val chartAxis: Color,
    val chartTooltipBackground: Color,
    val chartTooltipText: Color,

    // Text
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val textOnPrimary: Color,
    val textOnExpense: Color,

    // Borders
    val border: Color,
    val borderStrong: Color,
    val divider: Color,

    // Navigation
    val navigationBackground: Color,
    val navigationSelected: Color,
    val navigationUnselected: Color,
    val navigationIndicator: Color,

    // Status
    val success: Color,
    val successSoft: Color,
    val warning: Color,
    val warningSoft: Color,
    val error: Color,
    val errorSoft: Color,
    val info: Color,
    val infoSoft: Color,

    // Icons
    val iconPrimary: Color,
    val iconSecondary: Color,
    val iconDisabled: Color,
    val iconOnPrimary: Color
)