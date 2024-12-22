package com.xmichxl.walletmanapp.features.category.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.category.data.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) : ViewModel() {
    // LiveData to observe categories
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    init {
        loadCategories()
    }

    private fun loadCategories(){
        viewModelScope.launch {
            repository.getAllCategories().collect{ items ->
                _categories.value = if (items.isNullOrEmpty()) emptyList() else items
            }
        }
    }

    fun addCategory(category: Category) = viewModelScope.launch { repository.insert(category) }
    fun updateCategory(category: Category) = viewModelScope.launch { repository.update(category) }
    fun deleteCategory(category: Category) = viewModelScope.launch { repository.delete(category) }

}
