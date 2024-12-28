package com.xmichxl.walletmanapp.features.shared.data

import android.util.Log
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentDateTimeIso
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountDao
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AccountTransactionRepository(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) {
    suspend fun createAccountWithInitialTransaction(account: Account, initialBalance: Double) {
        withContext(Dispatchers.IO) {
            val accountId = accountDao.insert(account) // Insert account, get ID if autogenerated

            if (initialBalance > 0) {
                // Add initial balance as an "Adjustment" transaction
                val initialTransaction = Transaction(
                    amount = initialBalance,
                    description = "Initial Balance ${account.name}",
                    type = TransactionType.ADJUSTMENT.value,
                    accountToId = accountId.toInt(), // Link accountToId
                    accountFromId = null,
                    date = getCurrentDateTimeIso()
                )
                transactionDao.insert(initialTransaction)
            }
        }
    }

    suspend fun createTransactionAndUpdateBalance(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            transactionDao.insert(transaction)
            updateAccountBalance(transaction) // New Transaction with the adjustment (that's why doesn't need to pass oldAmount)
        }
    }

    suspend fun updateTransactionAndUpdateBalance(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            // Fetch the old transaction from the database
            val oldTransaction = transactionDao.getTransactionById(transaction.id).first()

            // 1. Update the transaction in the database
            transactionDao.update(transaction)

            // 2. Adjust the account balance using the old and new amounts
            updateAccountBalance(transaction, oldTransaction.amount)
        }
    }

    suspend fun deleteTransactionAndUpdateBalance(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            // Reverse balance changes before deleting transaction
            transaction.accountToId?.let { accountToId ->
                val account = accountDao.getAccountById(accountToId).first() // Collect Flow
                val newBalance = account.balance - transaction.amount
                accountDao.update(account.copy(balance = newBalance))
            }
            transaction.accountFromId?.let { accountFromId ->
                val account = accountDao.getAccountById(accountFromId).first() // Collect Flow
                val newBalance = account.balance + transaction.amount
                accountDao.update(account.copy(balance = newBalance))
            }

            // Delete transaction
            transactionDao.delete(transaction)
        }
    }

    private suspend fun updateAccountBalance(transaction: Transaction, oldAmount: Double = 0.0) {
        val amountDifference = transaction.amount - oldAmount

        // Update Account balance based on transaction type
        when (transaction.type) {
            TransactionType.EXPENSE.value -> {
                transaction.accountFromId?.let { accountFromId ->
                    val account = accountDao.getAccountById(accountFromId).first()
                    val newBalance = account.balance - amountDifference
                    accountDao.update(account.copy(balance = newBalance))
                }
            }

            TransactionType.INCOME.value -> {
                transaction.accountToId?.let { accountToId ->
                    val account = accountDao.getAccountById(accountToId).first()
                    val newBalance = account.balance + amountDifference
                    accountDao.update(account.copy(balance = newBalance))
                }
            }

            TransactionType.TRANSFER.value -> {
                transaction.accountFromId?.let { accountFromId ->
                    val accountFrom = accountDao.getAccountById(accountFromId).first()
                    val newBalanceFrom = accountFrom.balance - amountDifference
                    accountDao.update(accountFrom.copy(balance = newBalanceFrom))
                }
                transaction.accountToId?.let { accountToId ->
                    val accountTo = accountDao.getAccountById(accountToId).first()
                    val newBalanceTo = accountTo.balance + amountDifference
                    accountDao.update(accountTo.copy(balance = newBalanceTo))
                }
            }

            TransactionType.ADJUSTMENT.value -> {
                /*
                * If the new balance is higher, it's treated as adding money → Use accountToId.
                * If the new balance is lower, it's treated as subtracting money → Use accountFromId.
                */
                transaction.accountToId?.let { accountToId ->
                    val account = accountDao.getAccountById(accountToId).first()
                    accountDao.update(account.copy(balance = transaction.amount)) // Set the balance directly
                }
                transaction.accountFromId?.let { accountFromId ->
                    val account = accountDao.getAccountById(accountFromId).first()
                    accountDao.update(account.copy(balance = transaction.amount)) // Set the balance directly
                }
            }

            else -> {
                Log.e("Transaction", "Unknown transaction type: ${transaction.type}")
            }
        }
    }
}
