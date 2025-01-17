package com.xmichxl.walletmanapp.features.transaction.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.core.utils.getDateRangeFor
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.category.data.CategoryRepository
import com.xmichxl.walletmanapp.features.shared.data.AccountTransactionRepository
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.subcategory.data.SubcategoryRepository
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionRepository
import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithDetails
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
    // Control methods that triggers only in specific Views
    private var hasLoadedCategories = false
    private var isObservingCategorySelection = false

    // StateFlow to expose the list of transactions
    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList = _transactionList.asStateFlow()

    private val _transactionsWithDetails = MutableStateFlow<List<TransactionWithDetails>>(emptyList())
    val transactionsWithDetails = _transactionsWithDetails.asStateFlow()

    // StateFlow for the selected transaction (for editing)
    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction = _selectedTransaction.asStateFlow()

    private val _selectedTransactionWithDetails = MutableStateFlow<TransactionWithDetails?>(null)
    val selectedTransactionWithDetails = _selectedTransactionWithDetails.asStateFlow()

    private val _transactionsByRange = MutableStateFlow<List<TransactionWithDetails>>(emptyList())
    val transactionsByRange = _transactionsByRange.asStateFlow()

    private val _filteredTransactions = MutableStateFlow<List<TransactionWithDetails>>(emptyList())
    val filteredTransactions = _filteredTransactions.asStateFlow()

    private val _categoryList = MutableStateFlow<List<Category>>(emptyList())
    val categoryList = _categoryList.asStateFlow()

    private val _subcategoryList = MutableStateFlow<List<Subcategory>>(emptyList())
    val subcategoryList = _subcategoryList.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    init {
        //getTransactionsWithDetails()
        getTransactionsByRange("lastWeek")
    }

    // Function to load transactions
    fun getTransactionsWithDetails() {
        viewModelScope.launch {
            repository.getAllTransactionsWithDetails().collect { items ->
                Log.d("Transactionwithdetails", items.toString())
                _transactionsWithDetails.value = items
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

    fun setSelectedCategoryId(id: Int) {
        if (_selectedCategoryId.value == id) {
            _selectedCategoryId.value = null // Reset to ensure observer triggers
        }
        _selectedCategoryId.value = id
    }

    //Listener of selectedCategoryId that will trigger when the View calls setSelectedCategoryId(id)
    private fun observeCategorySelection() {
        viewModelScope.launch {
            selectedCategoryId.collect { id ->
                id?.let {
                    clearSubcategories()
                    loadSubcategories(it)
                }
            }
        }
    }

    // To trigger and load categories and subcategories (only in the specific views that calls these methods)
    fun loadCategoriesIfNeeded() {
        if (!hasLoadedCategories) {
            loadCategories()
            hasLoadedCategories = true
        }
    }
    fun observeCategorySelectionIfNeeded() {
        if (!isObservingCategorySelection) {
            observeCategorySelection()
            isObservingCategorySelection = true
        }
    }

    fun clearSubcategories() {
        _subcategoryList.value = emptyList()
    }
    fun resetCategoryAndSubcategories() {
        _selectedCategoryId.value = null
        clearSubcategories() // This already resets the subcategory list
    }

    fun getTransactionById(id: Long){
        viewModelScope.launch {
            repository.getTransactionById(id).collect { transaction ->
                _selectedTransaction.value = transaction
            }
        }
    }
    fun getTransactionWithDetailsById(id: Long){
        viewModelScope.launch {
            repository.getTransactionWithDetailsById(id).collect { transaction ->
                _selectedTransactionWithDetails.value = transaction
            }
        }
    }
    fun getTransactionsByRange(timeRange: String) {
        viewModelScope.launch {
            val (startDate, endDate) = getDateRangeFor(timeRange)

            repository.getTransactionsByDateRange(startDate, endDate)
                .collect { transactions ->
                    Log.d("Transactionbydaterange", transactions.toString())
                    _transactionsByRange.value = transactions
                }
        }
    }

    fun applyFilters(filters: Map<String, String>) {
        val dateRange = filters["dateRange"]?.takeIf { it != "All" }
        val type = filters["type"]?.takeIf { it != "All" }
        val accountId = filters["accountId"]?.takeIf { it != "All" }
        val categoryId = filters["categoryId"]?.takeIf { it != "All" }

        // Handle start and end date only if dateRange is not null
        val (startDate, endDate) = dateRange?.let { getDateRangeFor(it) } ?: Pair(null, null)

        Log.d("daterange: ", dateRange.toString())
        Log.d("type: ", type.toString())
        Log.d("account id: ", accountId.toString())
        Log.d("category id: ", categoryId.toString())

        viewModelScope.launch {
            repository.getFilteredTransactions(startDate, endDate, accountId?.toInt(), type, categoryId?.toInt())
                .collect { transactions ->
                    _filteredTransactions.value = transactions
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