package com.gdproduct.rendana.ui.screens.report

import androidx.compose.ui.graphics.Color
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.domain.model.TransactionType

fun buildAnalysisCategories(
    transactions: List<TransactionEntity>,
    categories: List<TransactionCategoryEntity>,
    type: TransactionType
): List<AnalysisCategory> {
    val categoryMap = categories.associateBy { it.id }
    val colors = listOf(
        Color(0xFFDC5050),
        Color(0xFFF59E0B),
        Color(0xFF0F8A68),
        Color(0xFF6366F1),
        Color(0xFF0EA5E9),
        Color(0xFFEC4899),
        Color(0xFF8B5CF6),
        Color(0xFF84CC16),
        Color(0xFF64748B),
        Color(0xFF14B8A6)
    )
	
    return transactions
        .filter { it.type == type.name }
        .groupBy { it.categoryId }
        .mapNotNull { (categoryId, categoryTransactions) ->
		
            val category = categoryMap[categoryId]
                ?: return@mapNotNull null
			
            AnalysisCategory(
                name = category.name,
                amount = categoryTransactions.sumOf { it.amount },
                color = colors[
                    categories.indexOfFirst { it.id == categoryId }
                        .coerceAtLeast(0) % colors.size
                ]
            )
        }
        .sortedByDescending { it.amount }
}