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
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // LiveData to observe categories
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    // Insert a category and fetch all categories
    fun addCategoryAndFetch(name: String) {
        viewModelScope.launch {
            // Insert a new category
            categoryRepository.insertCategory(Category(name = name))

            // Fetch all categories and update LiveData
            categoryRepository.getAllCategories().collect { categoryList ->
                _categories.postValue(categoryList)
            }
        }
    }
}
