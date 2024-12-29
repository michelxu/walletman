package com.xmichxl.walletmanapp.features.transaction.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xmichxl.walletmanapp.features.analytics.data.CategoryAnalytics
import com.xmichxl.walletmanapp.features.analytics.data.DailySpendingTrend
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

    @Query("""
        SELECT * FROM transactions
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<TransactionWithDetails>>

    // **************** Transaction With Details
    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionWithDetailsById(id: Long): Flow<TransactionWithDetails>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions")
    fun getAllTransactionsWithDetails(): Flow<List<TransactionWithDetails>>

    // **************** Analytics
    @Query("""
        SELECT categoryId, SUM(amount) AS total
        FROM transactions
        WHERE type = 'Expense' AND date BETWEEN :startDate AND :endDate
        GROUP BY categoryId
        ORDER BY total DESC
    """)
    fun getCategoryAnalytics(startDate: String, endDate: String): Flow<List<CategoryAnalytics>>

    @Query("""
        SELECT date, SUM(amount) AS total
        FROM transactions
        WHERE type = 'Expense' AND date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getDailySpendingTrend(startDate: String, endDate: String): Flow<List<DailySpendingTrend>>

}