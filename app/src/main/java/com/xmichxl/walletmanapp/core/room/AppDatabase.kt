package com.xmichxl.walletmanapp.core.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountDao
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.category.data.CategoryDao
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.subcategory.data.SubcategoryDao
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Config file
@Database(entities = [Transaction::class, Account::class, Category::class, Subcategory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun subcategoryDao(): SubcategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "walletman_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }

        // RoomDatabase.Callback for preloading data
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // Preload data using Coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        val categoryDao = database.categoryDao()
                        val subcategoryDao = database.subcategoryDao()

                        // Insert categories
                        val foodCategoryId = categoryDao.insert(Category(name = "Food"))
                        val transportCategoryId = categoryDao.insert(Category(name = "Transport"))

                        // Insert subcategories
                        subcategoryDao.insert(Subcategory(name = "Groceries", categoryId = foodCategoryId.toInt()))
                        subcategoryDao.insert(Subcategory(name = "Fuel", categoryId = transportCategoryId.toInt()))
                    }
                }
            }
        }
    }

}
