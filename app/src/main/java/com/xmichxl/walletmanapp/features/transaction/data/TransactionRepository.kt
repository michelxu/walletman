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
            .flowOn(Dispatchers.IO)
            .conflate()
    }

    fun getTransactionById(id: Long): Flow<Transaction> {
        return transactionDao.getTransactionById(id)
            .flowOn(Dispatchers.IO)
            .conflate()
    }
}