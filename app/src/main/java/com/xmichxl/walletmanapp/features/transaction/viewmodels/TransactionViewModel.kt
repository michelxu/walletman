package com.xmichxl.walletmanapp.features.transaction.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.category.data.CategoryRepository
import com.xmichxl.walletmanapp.features.shared.data.AccountTransactionRepository
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.subcategory.data.SubcategoryRepository
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionRepository
import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithAccounts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val sharedRepository: AccountTransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val subcategoryRepository: SubcategoryRepository
): ViewModel() {
    // StateFlow to expose the list of transactions
    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList = _transactionList.asStateFlow()

    private val _transactionsWithAccounts = MutableStateFlow<List<TransactionWithAccounts>>(emptyList())
    val transactionsWithAccounts = _transactionsWithAccounts.asStateFlow()

    // StateFlow for the selected transaction (for editing)
    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction = _selectedTransaction.asStateFlow()

    private val _selectedTransactionWithAccount = MutableStateFlow<TransactionWithAccounts?>(null)
    val selectedTransactionWithAccount = _selectedTransactionWithAccount.asStateFlow()

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _subcategoryList = MutableStateFlow<List<Subcategory>>(emptyList())
    val subcategoryList = _subcategoryList.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    fun setSelectedCategoryId(id: Int) {
        _selectedCategoryId.value = id
    }

    init {
        //loadTransactions()
        loadTransactionsWithAccounts()
        loadCategories()

        viewModelScope.launch {
            selectedCategoryId.collect { id ->
                id?.let { loadSubcategories(it) }
            }
        }
    }

    // Function to load transactions
    private fun loadTransactionsWithAccounts() {
        viewModelScope.launch {
            repository.getAllTransactionsWithAccounts().collect { items ->
                _transactionsWithAccounts.value = items
            }
        }
    }
    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { items ->
                _transactionList.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { items ->
                _categoryList.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    private fun loadSubcategories(id: Int) {
        viewModelScope.launch {
            subcategoryRepository.getSubcategoriesByCategoryId(id).collect { items ->
                _subcategoryList.value = if (items.isNullOrEmpty()) emptyList() else items
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
    fun getTransactionWithAccountById(id: Long){
        viewModelScope.launch {
            repository.getTransactionWithAccountById(id).collect { transaction ->
                _selectedTransactionWithAccount.value = transaction
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

    // Shared Repository
    fun createTransactionAndUpdateBalance(transaction: Transaction) = viewModelScope.launch { sharedRepository.createTransactionAndUpdateBalance(transaction) }
    fun deleteTransactionAndUpdateBalance(transaction: Transaction) = viewModelScope.launch { sharedRepository.deleteTransactionAndUpdateBalance(transaction) }
    fun updateTransactionAndUpdateBalance(transaction: Transaction) = viewModelScope.launch { sharedRepository.updateTransactionAndUpdateBalance(transaction) }
}