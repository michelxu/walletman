package com.xmichxl.walletman.features.subcategory.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.xmichxl.walletman.features.category.data.Category

@Entity(
    tableName = "subcategories",
    foreignKeys = [
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["category_id"])
    ]
)
data class SubCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val categoryId: Int // Links to Category
)
