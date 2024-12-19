package com.xmichxl.walletmanapp.features.transaction.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
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

    // Linked tables Transaction With Accounts
    fun getAllTransactionsWithAccounts(): Flow<List<TransactionWithAccounts>> {
        return transactionDao.getAllTransactionsWithAccounts()
    }

    fun getTransactionWithAccountById(id: Long): Flow<TransactionWithAccounts> {
        return transactionDao.getTransactionWithAccountsById(id)
    }
}