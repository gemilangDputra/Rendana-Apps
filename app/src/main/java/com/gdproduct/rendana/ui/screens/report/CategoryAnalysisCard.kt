package com.gdproduct.rendana.ui.screens.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import kotlin.math.roundToInt

data class AnalysisCategory(
    val name: String,
    val amount: Long,
    val color: Color
)

data class AnalysisTransaction(
    val id: Long,
    val name: String,
    val categoryName: String,
    val date: String,
    val amount: Long
)

private data class DisplayCategory(
    val name: String,
    val amount: Long,
    val color: Color,
    val isOther: Boolean = false
)

@Composable
fun CategoryAnalysisCard(
    title: String,
    totalAmount: Long,
    categories: List<AnalysisCategory>,
    transactions: List<AnalysisTransaction>,
    modifier: Modifier = Modifier,
    formatAmount: (Long) -> String,
    emptyMessage: String = "Belum ada data"
) {
    var showAllCategories by remember { mutableStateOf(false) }
    var showAllTransactions by remember { mutableStateOf(false) }
	
    val sortedCategories = categories
        .filter { it.amount > 0L }
        .sortedByDescending { it.amount }
	
    val smallCategories = sortedCategories.filter {
        totalAmount > 0L &&
            it.amount.toDouble() / totalAmount < 0.03
    }
	
    val majorCategories = sortedCategories.filterNot {
        totalAmount > 0L &&
            it.amount.toDouble() / totalAmount < 0.03
    }
	
    val needsOther = smallCategories.isNotEmpty() ||
        sortedCategories.size > 10
    
    val summaryCategories = if (needsOther) {
        val visibleMajor = majorCategories.take(9)
        val remainingMajor = majorCategories.drop(9)
        val otherCategories = remainingMajor + smallCategories
        val otherAmount = otherCategories.sumOf { it.amount }
		
        buildList {
            addAll(
                visibleMajor.map {
                    DisplayCategory(
                        name = it.name,
                        amount = it.amount,
                        color = it.color
                    )
                }
            )
			
            if (otherAmount > 0L) {
                add(
                    DisplayCategory(
                        name = "Lainnya",
                        amount = otherAmount,
                        color = Color.Gray,
                        isOther = true
                    )
                )
            }
        }
    } else {
        sortedCategories.map {
            DisplayCategory(
                name = it.name,
                amount = it.amount,
                color = it.color
            )
        }
    }.take(10)
	
    val sortedTransactions = transactions
        .sortedByDescending { it.amount }
        .take(10)
	
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
			
            if (sortedCategories.isEmpty() || totalAmount <= 0L) {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DonutChart(
                        categories = summaryCategories,
                        modifier = Modifier.size(116.dp),
                        centerText = formatAmount(totalAmount)
                    )
					
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        summaryCategories.forEach { category ->
                            CategoryLegendRow(
                                category = category,
                                totalAmount = totalAmount
                            )
                        }
                    }
                }
				
                if (sortedCategories.size > summaryCategories.size ||
                    smallCategories.isNotEmpty()
                ) {
                    Text(
                        text = if (showAllCategories) {
                            "Sembunyikan kategori"
                        } else {
                            "Lihat semua kategori"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                showAllCategories = !showAllCategories
                            }
                            .padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
				
                if (showAllCategories) {
                    HorizontalDivider()
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 240.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        items(
                            items = sortedCategories,
                            key = { it.name }
                        ) { category ->
                            Column {
                                CategoryLegendRow(
                                    category = DisplayCategory(
                                        name = category.name,
                                        amount = category.amount,
                                        color = category.color
                                    ),
                                    totalAmount = totalAmount
                                )
								
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        vertical = 8.dp
                                    ),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }
			
            HorizontalDivider()
            Text(
                text = "10 Transaksi Terbesar",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
			
            if (sortedTransactions.isEmpty()) {
                Text(
                    text = "Belum ada transaksi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val visibleTransactions = if (showAllTransactions) {
                    sortedTransactions
                } else {
                    sortedTransactions.take(3)
                }

                visibleTransactions.forEachIndexed { index, transaction ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    TransactionRow(
                        transaction = transaction,
                        formatAmount = formatAmount
                    )
                }
				
                if (sortedTransactions.size > 3) {
                    Text(
                        text = if (showAllTransactions) {
                            "Sembunyikan transaksi"
                        } else {
                            "Lihat semua transaksi"
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                showAllTransactions = !showAllTransactions
                            }
                            .padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    categories: List<DisplayCategory>,
    centerText: String,
    modifier: Modifier = Modifier
) {
    val total = categories.sumOf { it.amount }
		
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(116.dp)) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val arcSize = androidx.compose.ui.geometry.Size(
                diameter,
                diameter
            )
			
            val topLeft = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )
			
            if (total <= 0L) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )
            } else {
                var startAngle = -90f
                categories.forEach { category ->
                    val sweepAngle =
                        category.amount.toFloat() / total.toFloat() * 360f
					
                    drawArc(
                        color = category.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Butt
                        )
                    )
					
                    startAngle += sweepAngle
                }
            }
        }
		
        Text(
            text = centerText,
            modifier = Modifier
                .padding(horizontal = 22.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CategoryLegendRow(
    category: DisplayCategory,
    totalAmount: Long
) {
    val percentage = if (totalAmount > 0L) {
        (category.amount.toDouble() / totalAmount * 100).roundToInt()
    } else {
        0
    }
	
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(category.color)
        )
		
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
		
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TransactionRow(
    transaction: AnalysisTransaction,
    formatAmount: (Long) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
			
            Text(
                text = "${transaction.categoryName} · ${transaction.date}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
		
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formatAmount(transaction.amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
