package com.xmichxl.walletmanapp.features.account.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.data.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: AccountRepository): ViewModel() {
    // StateFlow to expose the list of transactions
    private val _accountList = MutableStateFlow<List<Account>>(emptyList())
    val accountList = _accountList.asStateFlow()

    // StateFlow for the selected account (for editing)
    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount = _selectedAccount.asStateFlow()

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

    fun getAccountById(id: Int){
        viewModelScope.launch {
            repository.getAccountById(id).collect { account ->
                _selectedAccount.value = account
            }
        }
    }

    fun addAccount(account: Account) = viewModelScope.launch { repository.addAccount(account) }
    fun updateAccount(account: Account) = viewModelScope.launch { repository.updateAccount(account) }
    fun deleteAccount(account: Account) = viewModelScope.launch { repository.deleteAccount(account) }
}