package com.xmichxl.walletman.core.di

import android.content.Context
import androidx.room.Room
import com.xmichxl.walletman.features.account.data.AccountDao
import com.xmichxl.walletman.core.room.AppDatabase
import com.xmichxl.walletman.features.transaction.data.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Provide the TransactionDao
    @Singleton
    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    // Provide the AccountDao
    @Singleton
    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.accountDao()
    }

    // Provide the Room Database
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "walletman_db" // Database name
        ).fallbackToDestructiveMigration()
            .build()
    }
}