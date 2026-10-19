package com.gdproduct.rendana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_categories")
data class TransactionCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val type: String,
    val name: String
)
