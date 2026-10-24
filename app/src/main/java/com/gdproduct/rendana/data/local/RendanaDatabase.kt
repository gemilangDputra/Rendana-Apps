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
                        Triple("EXPENSE", "Makanan & minuman", "restaurant"),
                        Triple("EXPENSE", "Belanja", "shoppingBag"),
                        Triple("EXPENSE", "Hiburan", "redeem"),
                        Triple("EXPENSE", "Kesehatan", "healthcross"),
                        Triple("EXPENSE", "Transportasi", "transportation"),
                        Triple("EXPENSE", "Pendidikan", "school"),
                        Triple("EXPENSE", "Tagihan", "receipt"),
                        Triple("EXPENSE", "Lainnya", "category"),
						
                        Triple("INCOME", "Gaji", "payments"),
                        Triple("INCOME", "Bisnis", "store"),
                        Triple("INCOME", "Bonus", "redeem"),
                        Triple("INCOME", "Hadiah", "redeem"),
                        Triple("INCOME", "Investasi", "trendingUp"),
                        Triple("INCOME", "Lainnya", "category")
                    )

                    defaultCategories.forEach { (type, name, iconName) ->
                        db.execSQL(
                            """
                            INSERT INTO transaction_categories
                                (type, name, iconName)
                            VALUES (?, ?, ?)
                            """.trimIndent(),
                            arrayOf(type, name, iconName)
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
