package com.gdproduct.rendana.ui.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.ui.icons.TrendingDown
import com.gdproduct.rendana.ui.icons.TrendingFlat
import com.gdproduct.rendana.ui.icons.TrendingUp
import com.gdproduct.rendana.ui.theme.RendanaThemeColors
import com.gdproduct.rendana.utils.AmountFormat
import com.gdproduct.rendana.utils.formatAmount

import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ReportSummaryCard(
    netBalance: Long,
    totalIncome: Long,
    totalExpense: Long,
    previousNetBalance: Long
) {
    val change = netBalance.toDouble() -
        previousNetBalance.toDouble()
	
    val percentage = if (previousNetBalance != 0L) {
        change / abs(previousNetBalance.toDouble()) * 100
    } else {
        0.0
    }
	
    val trendColor = when {
        change > 0 -> RendanaThemeColors.income
        change < 0 -> RendanaThemeColors.expense
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
	
    val trendIcon = when {
        change > 0 -> TrendingUp
        change < 0 -> TrendingDown
        else -> TrendingFlat
    }
	
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = RendanaThemeColors.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Ikhtisar keuangan",
                color = RendanaThemeColors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
			
            Text(
                text = "Selisih Bersih",
                color = RendanaThemeColors.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
			
            Text(
                text = formatAmount(
                    netBalance,
                    AmountFormat.FULL
                ),
                color = RendanaThemeColors.incomeText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
			
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = trendIcon,
                    contentDescription = when {
                        change > 0 -> "Meningkat"
                        change < 0 -> "Menurun"
                        else -> "Tidak berubah"
                    },
                    tint = trendColor
                )
				
                val comparisonText = when {
                    previousNetBalance == 0L && netBalance != 0L ->
                        "Belum ada pembanding"

                    else ->
                        "${percentage.roundToInt().let { abs(it) }}% dibanding periode sebelumnya"
                }
				
                Text(
                    text = comparisonText,
                    color = trendColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
		
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = RendanaThemeColors.summaryIncomeCard,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Pendapatan",
                    color = RendanaThemeColors.incomeText,
                    style = MaterialTheme.typography.labelMedium
                )
				
                Text(
                    text = formatAmount(
                        totalIncome,
                        AmountFormat.FULL
                    ),
                    color = RendanaThemeColors.incomeText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
			
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = RendanaThemeColors.summaryExpenseCard,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Pengeluaran",
                    color = RendanaThemeColors.expenseText,
                    style = MaterialTheme.typography.labelMedium
                )
				
                Text(
                    text = formatAmount(
                        totalExpense,
                        AmountFormat.FULL
                    ),
                    color = RendanaThemeColors.expenseText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}