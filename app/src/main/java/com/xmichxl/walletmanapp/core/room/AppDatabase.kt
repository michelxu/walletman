package com.xmichxl.walletmanapp.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountDao
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao

// Config file
@Database(entities = [Transaction::class, Account::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // DAO for transactions
    abstract fun transactionDao(): TransactionDao

    // DAO for accounts
    abstract fun accountDao(): AccountDao
}