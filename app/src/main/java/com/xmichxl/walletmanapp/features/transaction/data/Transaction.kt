package com.xmichxl.walletmanapp.features.transaction.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xmichxl.walletmanapp.core.utils.getCurrentTimestamp
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountFromId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountToId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Subcategory::class,
            parentColumns = ["id"],
            childColumns = ["subcategoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("accountFromId"), Index("accountToId"), Index("categoryId"), Index("subcategoryId")]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val date: String,                               // Store as ISO-8601 format (e.g., "2024-01-01")
    val type: String,                               // "Income", "Expense", "Transfer", "Adjustment"

    val accountFromId: Int? = null,                 // Nullable for income
    val accountToId: Int? = null,                   // Nullable for expenses
    val categoryId: Int? = null,                    // Nullable for income and transfers
    val subcategoryId: Int? = null,                 // Nullable for income and transfers

    val createdAt: String = getCurrentTimestamp(),
    val lastUpdated: String = getCurrentTimestamp()
)


/*
* Income: Only accountToId is used.
* Expense: Only accountFromId is used.
* Transfer: Both accountFromId and accountToId are used.
*
* For Adjustment works like Income or Expense.
* */
