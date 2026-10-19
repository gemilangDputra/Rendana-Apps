package com.gdproduct.rendana.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionCategoryDao {
    @Query("SELECT * FROM transaction_categories ORDER BY name ASC")
    fun getAll(): Flow<List<TransactionCategoryEntity>>
    
    @Query("SELECT * FROM transaction_categories WHERE type = :type ORDER BY name ASC")  
    fun getByType(type: String): Flow<List<TransactionCategoryEntity>>  
    
    @Insert  
    suspend fun insert(category: TransactionCategoryEntity)  
    @Delete  
    suspend fun delete(category: TransactionCategoryEntity)  
    @Update  
    suspend fun update(category: TransactionCategoryEntity)
    
}