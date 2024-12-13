package com.xmichxl.walletman.features.transaction.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletman.features.transaction.data.Transaction
import com.xmichxl.walletman.features.transaction.data.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val repository: TransactionRepository): ViewModel() {

    // StateFlow to expose the list of transactions
    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList = _transactionList.asStateFlow()

    init {
        loadTransactions()
    }

    // Function to load transactions
    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { items ->
                _transactionList.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch { repository.addTransaction(transaction) }
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch { repository.updateTransaction(transaction) }
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch { repository.deleteTransaction(transaction) }

}