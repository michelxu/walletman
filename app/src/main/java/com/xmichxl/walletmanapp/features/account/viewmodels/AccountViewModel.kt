package com.xmichxl.walletmanapp.features.account.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentDateTime
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountRepository
import com.xmichxl.walletmanapp.features.shared.data.AccountTransactionRepository
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val sharedRepository: AccountTransactionRepository
): ViewModel() {
    // StateFlow to expose the list of transactions
    private val _accountList = MutableStateFlow<List<Account>>(emptyList())
    val accountList = _accountList.asStateFlow()

    // StateFlow for the selected account (for editing)
    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount = _selectedAccount.asStateFlow()

    // Account for transaction `accountFrom`
    private val _selectedAccountFrom = MutableStateFlow<Account?>(null)
    val selectedAccountFrom = _selectedAccountFrom.asStateFlow()

    // Account for transaction `accountTo`
    private val _selectedAccountTo = MutableStateFlow<Account?>(null)
    val selectedAccountTo = _selectedAccountTo.asStateFlow()

    init {
        loadAccounts()
    }

    // Load all accounts when ViewModel is initialized
    private fun loadAccounts(){
        viewModelScope.launch {
            repository.getAllAccounts().collect { items ->
                _accountList.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    // Fetch the general selected account
    fun getAccountById(id: Int){
        viewModelScope.launch {
            repository.getAccountById(id).collect { account ->
                _selectedAccount.value = account
            }
        }
    }

    // Fetch the account for `accountFrom`
    fun getAccountFromById(id: Int) {
        viewModelScope.launch {
            repository.getAccountById(id).collect { account ->
                _selectedAccountFrom.value = account
            }
        }
    }

    // Fetch the account for `accountTo`
    fun getAccountToById(id: Int) {
        viewModelScope.launch {
            repository.getAccountById(id).collect { account ->
                _selectedAccountTo.value = account
            }
        }
    }

    fun addAccount(account: Account) = viewModelScope.launch { repository.addAccount(account) }
    fun updateAccount(account: Account) = viewModelScope.launch { repository.updateAccount(account) }
    fun deleteAccount(account: Account) = viewModelScope.launch { repository.deleteAccount(account) }

    // Example: Editing account and updating balance
    fun editAccountAndAdjustBalance(account: Account, oldBalance: Double) {
        viewModelScope.launch {
            val newBalance = account.balance
            val difference = newBalance - oldBalance

            if (difference != 0.0) {
                val adjustmentTransaction = Transaction(
                    amount = difference.absoluteValue,
                    type = TransactionType.ADJUSTMENT.value,
                    description = "Balance Adjustment",
                    accountToId = if (difference > 0) account.id else null,
                    accountFromId = if (difference < 0) account.id else null,
                    date = getCurrentDateTime()
                )
                sharedRepository.createTransactionAndUpdateBalance(adjustmentTransaction)
            }

            // Update the account details
            //repository.updateAccount(account.copy(balance = newBalance))
            repository.updateAccount(account)
        }
    }

    // Shared Repository
    fun createAccountWithTransaction(account: Account, initialBalance: Double) {
        viewModelScope.launch {
            sharedRepository.createAccountWithInitialTransaction(account, initialBalance)
        }
    }
}