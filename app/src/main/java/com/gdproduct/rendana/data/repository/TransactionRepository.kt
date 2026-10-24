package com.gdproduct.rendana.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.gdproduct.rendana.data.local.dao.TransactionDao
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.domain.model.TransactionFilter
import com.gdproduct.rendana.domain.model.TransactionSort
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    fun filterTransaction(
        filter: TransactionFilter = TransactionFilter(),
        sort: List<TransactionSort> = listOf(
            TransactionSort.DATE_NEWEST_FIRST
        ),
        limit: Int? = 50,
        query: String = ""
    ): Flow<List<TransactionEntity>> {
        val conditions = mutableListOf<String>()
        val args = mutableListOf<Any>()
		
		query.takeIf { it.isNotBlank() }?.let {
            conditions.add("(name LIKE ? OR merchantName LIKE ?)")
            args.add("%$it%")
            args.add("%$it%")
        }
		
		filter.merchantName?.takeIf { it.isNotBlank() }?.let { merchant ->
            conditions.add("merchantName LIKE ?")
            args.add("%$merchant%")
        }
		
        filter.type?.let { type ->
            conditions.add("type = ?")
            args.add(type.name)
        }
		
        filter.categoryId?.let { categoryId ->
            conditions.add("categoryId = ?")
            args.add(categoryId)
        }
		
        filter.minAmount?.let { minAmount ->
            conditions.add("amount >= ?")
            args.add(minAmount)
        }
		
        filter.maxAmount?.let { maxAmount ->
            conditions.add("amount <= ?")
            args.add(maxAmount)
        }
		
        filter.startDate?.let { startDate ->
            conditions.add("date >= ?")
            args.add(startDate)
        }
		
        filter.endDate?.let { endDate ->
            conditions.add("date <= ?")
            args.add(endDate)
        }
		
        val whereClause = if (conditions.isEmpty()) {
            ""
        } else {
            "WHERE ${conditions.joinToString(" AND ")}"
        }
		
        val orderClauses = sort.map { sortType ->
        when (sortType) {
            TransactionSort.TYPE_INCOME_FIRST ->
                "CASE WHEN type = 'INCOME' THEN 0 ELSE 1 END ASC"
            
            TransactionSort.TYPE_EXPENSE_FIRST ->
                "CASE WHEN type = 'EXPENSE' THEN 0 ELSE 1 END ASC"
            
            TransactionSort.AMOUNT_HIGH_TO_LOW ->
                "amount DESC"
            
            TransactionSort.AMOUNT_LOW_TO_HIGH ->
                "amount ASC"
            
            TransactionSort.DATE_NEWEST_FIRST ->
                "date DESC, id ASC"
            
            TransactionSort.DATE_OLDEST_FIRST ->
                "date ASC, id ASC"
            }
        }.toMutableList()
        
        orderClauses.add("id DESC")
        val orderClause = orderClauses.joinToString(", ")
        val safeLimit = limit?.coerceIn(1, 500)
        
        val sql = buildString {
            append("SELECT * FROM transactions ")
            append(whereClause)
            append(" ORDER BY ")
            append(orderClause)

        if (safeLimit != null) {
                append(" LIMIT ?")
                args.add(safeLimit)
            }
        }
        
        val query = SimpleSQLiteQuery(
            sql,
            args.toTypedArray()
        )
        
        return transactionDao.filterTransactions(query)
    }
	
	fun getAll(): Flow<List<TransactionEntity>> {
		return transactionDao.getAll()
	}
	
   suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }
	suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }
    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }
}