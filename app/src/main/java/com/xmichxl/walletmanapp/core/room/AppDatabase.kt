package com.xmichxl.walletmanapp.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountDao

// Config file
@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // DAO for accounts
    abstract fun accountDao(): AccountDao
}