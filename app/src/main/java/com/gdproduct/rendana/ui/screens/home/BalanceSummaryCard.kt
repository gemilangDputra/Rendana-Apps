package com.gdproduct.rendana.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.ui.theme.RendanaThemeColors

import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceSummaryCard(
    totalBalance: Long,
    monthlyIncome: Long,
    monthlyExpense: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = RendanaThemeColors.balanceCard,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Total Saldo",
                color = RendanaThemeColors.balanceText,
                style = MaterialTheme.typography.titleMedium
            )
			
            Text(
                text = formatRupiah(totalBalance),
                color = RendanaThemeColors.balanceText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
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
                    text = "Pendapatan Bulan Ini",
                    color = RendanaThemeColors.incomeText,
                    style = MaterialTheme.typography.labelMedium
                )
				
                Text(
                    text = formatRupiah(monthlyIncome),
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
                    text = "Pengeluaran Bulan Ini",
                    color = RendanaThemeColors.expenseText,
                    style = MaterialTheme.typography.labelMedium
                )
				
                Text(
                    text = formatRupiah(monthlyExpense),
                    color = RendanaThemeColors.expenseText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun formatRupiah(amount: Long): String {
    return NumberFormat.getCurrencyInstance(
        Locale("id", "ID")
    ).format(amount)
        .replace(",00", "")
}