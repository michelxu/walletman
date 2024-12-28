package com.xmichxl.walletmanapp.features.transaction.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {
    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    // Flow y LiveData manejan el manejo de entradas,
    // por lo tanto no es necesario usar corrutinas

    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionDao.getTransactionById(id)
    }

    // ********************** Linked tables Transaction With Details (Account, Cat, Subcat)
    fun getTransactionWithDetailsById(id: Long): Flow<TransactionWithDetails>{
        return transactionDao.getTransactionWithDetailsById(id)
    }

    fun getAllTransactionsWithDetails(): Flow<List<TransactionWithDetails>>{
        return transactionDao.getAllTransactionsWithDetails()
    }

    fun getTransactionsByDateRange(startDate: String, endDate: String): Flow<List<TransactionWithDetails>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate)
    }
}