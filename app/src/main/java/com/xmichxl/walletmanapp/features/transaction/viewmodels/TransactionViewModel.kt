package com.xmichxl.walletmanapp.features.transaction.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionRepository
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

    // StateFlow for the selected transaction (for editing)
    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction = _selectedTransaction.asStateFlow()

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

    fun getTransactionById(id: Long){
        viewModelScope.launch {
            repository.getTransactionById(id).collect { transaction ->
                _selectedTransaction.value = transaction
            }
        }
    }

    fun addTransaction(transaction: Transaction) = viewModelScope.launch { repository.addTransaction(transaction) }
    fun updateTransaction(transaction: Transaction) {
        try {
            Log.d("TransactionUpdate", "Before update: $transaction")
            viewModelScope.launch { repository.updateTransaction(transaction) }
            Log.d("TransactionUpdate", "After update: Update completed")
        } catch (e: Exception){
            Log.e("UpdateTransaction", "Error updating transaction", e)
        }
    }
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch { repository.deleteTransaction(transaction) }

}