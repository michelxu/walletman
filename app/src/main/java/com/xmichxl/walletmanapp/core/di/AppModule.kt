package com.xmichxl.walletmanapp.core.di

import android.content.Context
import androidx.room.Room
import com.xmichxl.walletmanapp.core.room.AppDatabase
import com.xmichxl.walletmanapp.features.account.data.AccountDao
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.category.data.CategoryDao
import com.xmichxl.walletmanapp.features.shared.data.AccountTransactionRepository
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.subcategory.data.SubcategoryDao
import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAccountTransactionRepository(
        accountDao: AccountDao,
        transactionDao: TransactionDao
    ): AccountTransactionRepository {
        return AccountTransactionRepository(accountDao, transactionDao)
    }

    // Provide the AccountDao
    @Singleton
    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.accountDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Singleton
    @Provides
    fun provideSubcategoryDao(appDatabase: AppDatabase): SubcategoryDao {
        return appDatabase.subcategoryDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "walletman_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            val categoryDao = db.categoryDao()
            val subcategoryDao = db.subcategoryDao()

            if (categoryDao.getAllCategories().firstOrNull()?.isEmpty() == true) {
                // Insert categories
                val foodCategoryId = categoryDao.insert(Category(name = "Food"))
                val transportCategoryId = categoryDao.insert(Category(name = "Transport"))

                // Insert subcategories
                subcategoryDao.insert(Subcategory(name = "Groceries", categoryId = foodCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Fuel", categoryId = transportCategoryId.toInt()))
            }
        }

        return db
    }
}