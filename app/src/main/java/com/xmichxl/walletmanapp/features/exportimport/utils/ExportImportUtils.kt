package com.xmichxl.walletmanapp.features.exportimport.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountDao
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object ExportImportUtils {

    suspend fun exportDatabase(accountDao: AccountDao, transactionDao: TransactionDao): String {
        return withContext(Dispatchers.IO) {
            val accounts = accountDao.getAllAccounts().first()
            val transactions = transactionDao.getAllTransactions().first()

            val dataMap = mapOf(
                "accounts" to accounts,
                "transactions" to transactions
            )
            Gson().toJson(dataMap)
        }
    }

    suspend fun importDatabase(jsonData: String, accountDao: AccountDao, transactionDao: TransactionDao) {
        withContext(Dispatchers.IO) {
            val dataMap: Map<String, List<Any>> = Gson().fromJson(jsonData, object : TypeToken<Map<String, List<LinkedTreeMap<String, Any>>>>() {}.type)

            // Deserialize accounts
            val accounts = dataMap["accounts"]?.map { item ->
                Gson().fromJson(Gson().toJson(item), Account::class.java)
            }

            // Deserialize transactions
            val transactions = dataMap["transactions"]?.map { item ->
                Gson().fromJson(Gson().toJson(item), Transaction::class.java)
            }

            accounts?.let { accountDao.insertAll(it) }
            transactions?.let { transactionDao.insertAll(it) }
        }
    }
}