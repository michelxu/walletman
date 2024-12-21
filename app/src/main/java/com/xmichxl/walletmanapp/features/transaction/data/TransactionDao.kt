package com.xmichxl.walletmanapp.features.transaction.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: Long): Flow<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    // Transaction with Account
    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionWithAccountsById(id: Long): Flow<TransactionWithAccounts>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransactionsWithAccounts(): Flow<List<TransactionWithAccounts>>
}