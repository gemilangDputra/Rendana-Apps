package com.gdproduct.rendana.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("""SELECT * FROM transactions ORDER BY id DESC""")
    fun getAll(): Flow<List<TransactionEntity>>
	
    @RawQuery(
        observedEntities = [TransactionEntity::class]
    )
    fun filterTransactions(
        query: SupportSQLiteQuery
    ): Flow<List<TransactionEntity>>
	
	@Insert
    suspend fun insert(transaction: TransactionEntity)
	@Delete
    suspend fun delete(transaction: TransactionEntity)
	@Update
    suspend fun update(transaction: TransactionEntity)
}