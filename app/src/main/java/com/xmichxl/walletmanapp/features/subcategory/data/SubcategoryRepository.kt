package com.xmichxl.walletmanapp.features.subcategory.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubcategoryRepository @Inject constructor(private val subcategoryDao: SubcategoryDao) {
    suspend fun insert(subcategory: Subcategory) {
        subcategoryDao.insert(subcategory)
    }

    suspend fun update(subcategory: Subcategory) {
        subcategoryDao.update(subcategory)
    }

    suspend fun delete(subcategory: Subcategory) {
        subcategoryDao.delete(subcategory)
    }

    fun getAllSubcategories(): Flow<List<Subcategory>> {
        return subcategoryDao.getAllSubcategories()
    }

    fun getSubcategoryById(id: Int): Flow<Subcategory> {
        return subcategoryDao.getSubcategoryById(id)
    }

    fun getSubcategoriesByCategoryId(categoryId: Int): Flow<List<Subcategory>> {
        return subcategoryDao.getSubcategoriesByCategoryId(categoryId)
    }
}