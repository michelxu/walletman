package com.xmichxl.walletmanapp.features.subcategory.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.subcategory.data.SubcategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubcategoryViewModel @Inject constructor(private val repository: SubcategoryRepository): ViewModel() {
    // StateFlow to expose the list of subcategories
    private val _subcategoryList = MutableStateFlow<List<Subcategory>>(emptyList())
    val subcategoryList = _subcategoryList.asStateFlow()

    init {
        loadSubcategories()
    }

    // Load all subcategories when ViewModel is initialized
    private fun loadSubcategories() {
        viewModelScope.launch {
            repository.getAllSubcategories().collect { items ->
                _subcategoryList.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    fun addSubcategory(subcategory: Subcategory) = viewModelScope.launch { repository.insert(subcategory) }
    fun updateSubcategory(subcategory: Subcategory) = viewModelScope.launch { repository.update(subcategory) }
    fun deleteSubcategory(subcategory: Subcategory) = viewModelScope.launch { repository.delete(subcategory) }

}