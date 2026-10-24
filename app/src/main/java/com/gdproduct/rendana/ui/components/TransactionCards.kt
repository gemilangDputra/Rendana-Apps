package com.gdproduct.rendana.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionCard(
    title: String,
    amount: Long,
    type: TransactionType,
    category: String,
    date: String,
    icon: ImageVector,
    note: String? = null,
	merchant: String? = null,
    modifier: Modifier = Modifier
    ) {
    val colors = RendanaThemeColors
    val isIncome = type == TransactionType.INCOME
	
    val amountColor =
	if (isIncome) {
        colors.incomeText
    } else {
        colors.expenseText
    }
	
    val iconBoxColor =
	if (isIncome) {
        colors.incomeDark
    } else {
        colors.expenseDark
    }
	
    val iconColor =
	if (isIncome) {
        colors.incomeLight
    } else {
        colors.expenseLight
    }
	
    val amountPrefix = if (isIncome) "+" else "−"
    val formattedAmount = NumberFormat
        .getNumberInstance(Locale("id", "ID"))
        .apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
        .format(amount.coerceAtLeast(0L))
    
	val displayMerchant = merchant?.takeIf { it.isNotBlank() }
	val displayNote = note?.takeIf { it.isNotBlank() }
	
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = iconBoxColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
			
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
				
                Text(
                    text = "$date • $category",
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
				
				if (displayMerchant != null) {
                    Text(
                        text = displayMerchant,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
				
                if (displayNote != null) {
                    Text(
                        text = displayNote,
                        color = colors.textTertiary,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
			
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$amountPrefix" + "Rp$formattedAmount",
                color = amountColor,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}