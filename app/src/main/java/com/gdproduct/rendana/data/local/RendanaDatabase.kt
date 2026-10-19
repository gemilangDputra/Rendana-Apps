
package com.gdproduct.rendana.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gdproduct.rendana.data.local.dao.TransactionDao
import com.gdproduct.rendana.data.local.dao.TransactionCategoryDao
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity

@Database(
    entities = [
        TransactionEntity::class,
        TransactionCategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class RendanaDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun transactionCategoryDao(): TransactionCategoryDao
	
    companion object {
        @Volatile
        private var INSTANCE: RendanaDatabase? = null
        
        private val databaseCallback =
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
				    
                    val defaultCategories = listOf(
                        "EXPENSE" to "Makanan & minuman",
                        "EXPENSE" to "Belanja",
                        "EXPENSE" to "Hiburan",
                        "EXPENSE" to "Kesehatan",
                        "EXPENSE" to "Transportasi",
                        "EXPENSE" to "Pendidikan",
                        "EXPENSE" to "Tagihan",
                        "EXPENSE" to "Lainnya",
                        "INCOME" to "Gaji",
                        "INCOME" to "Bisnis",
                        "INCOME" to "Bonus",
                        "INCOME" to "Hadiah",
                        "INCOME" to "Investasi",
                        "INCOME" to "Lainnya"
                    )
					
                    defaultCategories.forEach { (type, name) ->
                        db.execSQL(
                            """
                            INSERT INTO transaction_categories (type, name)
                            VALUES (?, ?)
                            """.trimIndent(),
                            arrayOf(type, name)
                        )
                    }
                }
            }
		
        fun getDatabase(context: Context): RendanaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RendanaDatabase::class.java,
                    "rendana_database"
                )
                    .addCallback(databaseCallback)
                    .build()
                    .also { database ->
                        INSTANCE = database
                    }
            }
        }
    }
}