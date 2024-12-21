package com.xmichxl.walletmanapp.features.subcategory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubcategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subcategory: Subcategory)

    @Update
    suspend fun update(subcategory: Subcategory)

    @Delete
    suspend fun delete(subcategory: Subcategory)

    @Query("SELECT * FROM subcategories")
    fun getAllSubcategories(): Flow<List<Subcategory>>

    @Query("SELECT * FROM subcategories WHERE id = :id")
    fun getSubcategoryById(id: Int): Flow<Subcategory>

    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId")
    fun getSubcategoriesByCategoryId(categoryId: Int): Flow<List<Subcategory>>
}