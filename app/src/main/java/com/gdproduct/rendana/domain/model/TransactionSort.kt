package com.gdproduct.rendana.domain.model

enum class TransactionSort {
    TYPE_INCOME_FIRST,
    TYPE_EXPENSE_FIRST,

    AMOUNT_HIGH_TO_LOW,
    AMOUNT_LOW_TO_HIGH,

    DATE_NEWEST_FIRST,
    DATE_OLDEST_FIRST
}