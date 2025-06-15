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
        transactionDao: TransactionDao,
        @ApplicationContext context: Context
    ): AccountTransactionRepository {
        return AccountTransactionRepository(accountDao, transactionDao, context)
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

    // Provide the Room Database
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
                val foodCategoryId = categoryDao.insert(Category(name = "Food", icon = "food", color = "Orange"))
                val shoppingCategoryId = categoryDao.insert(Category(name = "Shopping", icon = "shopping", color = "Magenta"))
                val housingCategoryId = categoryDao.insert(Category(name = "Housing", icon = "house", color = "Green"))
                val transportationCategoryId = categoryDao.insert(Category(name = "Transportation", icon = "transport", color = "Blue"))
                val vehicleCategoryId = categoryDao.insert(Category(name = "Vehicle", icon = "vehicle", color = "Silver"))
                val lifeCategoryId = categoryDao.insert(Category(name = "Life & Entertainment", icon = "entertainment", color = "Yellow"))
                val communicationCategoryId = categoryDao.insert(Category(name = "Communication", icon = "communication", color = "Teal"))
                val financialCategoryId = categoryDao.insert(Category(name = "Financial expenses", icon = "bank", color = "NavyBlue"))
                val incomeCategoryId = categoryDao.insert(Category(name = "Income", icon = "income", color = "Gold"))
                val otherCategoryId = categoryDao.insert(Category(name = "Other", icon = "other", color = "Gray"))

                // Insert subcategories
                // Food
                subcategoryDao.insert(Subcategory(name = "Groceries", categoryId = foodCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Delivery", categoryId = foodCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Restaurant", categoryId = foodCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = foodCategoryId.toInt()))

                // Shopping
                subcategoryDao.insert(Subcategory(name = "Clothes & shoes", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Drug-store", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Electronics", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Gifts", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Health and care", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Home", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Jewels", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Kids", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Pets", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Tools", categoryId = shoppingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = shoppingCategoryId.toInt()))

                // Housing
                subcategoryDao.insert(Subcategory(name = "Energy", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Water", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Gas", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Internet", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Maintainance & repairs", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Rent", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Services", categoryId = housingCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = housingCategoryId.toInt()))

                // Transportation
                subcategoryDao.insert(Subcategory(name = "App Ride", categoryId = transportationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Public transportation", categoryId = transportationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Taxi", categoryId = transportationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = transportationCategoryId.toInt()))

                // Vehicle
                subcategoryDao.insert(Subcategory(name = "Fuel", categoryId = vehicleCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Leasing", categoryId = vehicleCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Parking", categoryId = vehicleCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Vehicle insurance", categoryId = vehicleCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Vehicle maintainance", categoryId = vehicleCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = vehicleCategoryId.toInt()))

                // Life & Entertainment
                subcategoryDao.insert(Subcategory(name = "Sports & Fitness", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Books, audio & suscriptions", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Culture", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Education & development", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Health care", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Hobbies", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Holiday", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Gambling", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "TV & streaming", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Videogames", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Wellness", categoryId = lifeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = lifeCategoryId.toInt()))

                // Communcation
                subcategoryDao.insert(Subcategory(name = "Internet", categoryId = communicationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Phone", categoryId = communicationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Software", categoryId = communicationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Apps", categoryId = communicationCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = communicationCategoryId.toInt()))

                // Financial
                subcategoryDao.insert(Subcategory(name = "Advisory", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Charges & fees", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Fines", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Insurances", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Loans & interests", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Taxes", categoryId = financialCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = financialCategoryId.toInt()))

                // Income
                subcategoryDao.insert(Subcategory(name = "Income", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Dues", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Gifts", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Renting", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Gambling", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Refunds", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Sale", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Wage", categoryId = incomeCategoryId.toInt()))
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = incomeCategoryId.toInt()))

                // Other
                subcategoryDao.insert(Subcategory(name = "Other", categoryId = otherCategoryId.toInt()))
            }
        }

        return db
    }
}