package com.gdproduct.rendana.domain.model
import com.gdproduct.rendana.domain.model.TransactionType

data class TransactionFilter(
    val type: TransactionType? = null,
    val minAmount: Long? = null,
    val maxAmount: Long? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val categoryId: Int? = null
)