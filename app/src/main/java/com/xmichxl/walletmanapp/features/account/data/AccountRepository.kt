package com.xmichxl.walletmanapp.features.account.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AccountRepository @Inject constructor(private val accountDao: AccountDao) {

    suspend fun addAccount(account: Account) {
        accountDao.insert(account)
    }

    suspend fun updateAccount(account: Account) {
        accountDao.update(account)
    }

    suspend fun deleteAccount(account: Account) {
        accountDao.delete(account)
    }

    // Flow y LiveData manejan el manejo de entradas,
    // por lo tanto no es necesario usar corrutinas

    fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts()
            .flowOn(Dispatchers.IO)
            .conflate()
    }

    fun getAccountById(id: Int): Flow<Account> {
        return accountDao.getAccountById(id)
            .flowOn(Dispatchers.IO)
            .conflate()
    }
}