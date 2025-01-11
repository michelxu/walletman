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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @androidx.room.Transaction
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
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsWithDetails(): Flow<List<TransactionWithDetails>>

    @Query("""
    SELECT * FROM transactions
    WHERE (:startDate IS NULL OR date >= :startDate) 
    AND (:endDate IS NULL OR date <= :endDate) 
    AND (:accountId IS NULL OR accountFromId = :accountId OR accountToId = :accountId) 
    AND (:type IS NULL OR type = :type) 
    AND (:categoryId IS NULL OR categoryId = :categoryId)
    ORDER BY date DESC
""")
    fun getFilteredTransactions(
        startDate: String?,
        endDate: String?,
        accountId: Int?,
        type: String?,
        categoryId: Int?
    ): Flow<List<TransactionWithDetails>>

    // **************** Analytics
    @Query("""
        SELECT SUM(amount) AS total
        FROM transactions
        WHERE type = 'Expense' AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalSpent(startDate: String, endDate: String): Flow<Double?>

    @Query("""
        SELECT t.categoryId, c.name AS categoryName, c.color AS categoryColor, c.icon AS categoryIcon, SUM(t.amount) AS total
        FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE t.type = 'Expense'
        AND (:startDate IS NULL OR t.date >= :startDate)
        AND (:endDate IS NULL OR t.date <= :endDate)
        GROUP BY t.categoryId, c.name, c.color, c.icon
        ORDER BY total DESC
    """)
    fun getCategoryAnalytics(startDate: String?, endDate: String?): Flow<List<CategoryAnalytics>>

    @Query("""
        SELECT date, SUM(amount) AS total
        FROM transactions
        WHERE type = 'Expense' AND date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getDailySpendingTrend(startDate: String, endDate: String): Flow<List<DailySpendingTrend>>

}