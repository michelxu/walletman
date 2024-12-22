package com.xmichxl.walletmanapp.features.category.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {
    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun update(category: Category) {
        categoryDao.update(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    fun getCategoryById(id: Int): Flow<Category> {
        return categoryDao.getCategoryById(id)
    }
}