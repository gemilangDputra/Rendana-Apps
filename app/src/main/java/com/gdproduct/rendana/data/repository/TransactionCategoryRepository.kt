package com.gdproduct.rendana.data.repository

import com.gdproduct.rendana.data.local.dao.TransactionCategoryDao
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import kotlinx.coroutines.flow.Flow

class TransactionCategoryRepository(
    private val transactionCategoryDao: TransactionCategoryDao
) {
    fun getAll(): Flow<List<TransactionCategoryEntity>> {
        return transactionCategoryDao.getAll()
    }

    fun getByType(
        type: String
    ): Flow<List<TransactionCategoryEntity>> {
        return transactionCategoryDao.getByType(type)
    }

    suspend fun insertCategory(
        category: TransactionCategoryEntity
    ) {
        transactionCategoryDao.insert(category)
    }

    suspend fun updateCategory(
        category: TransactionCategoryEntity
    ) {
        transactionCategoryDao.update(category)
    }

    suspend fun deleteCategory(
        category: TransactionCategoryEntity
    ) {
        transactionCategoryDao.delete(category)
    }
}
