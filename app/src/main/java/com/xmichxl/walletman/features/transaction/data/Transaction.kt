package com.xmichxl.walletman.features.transaction.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.xmichxl.walletman.features.account.data.Account
import com.xmichxl.walletman.core.utils.getCurrentTimestamp

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = Account::class, parentColumns = ["id"], childColumns = ["accountFromId"]),
        ForeignKey(entity = Account::class, parentColumns = ["id"], childColumns = ["accountToId"]),
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val date: String,                               // Store as ISO-8601 format (e.g., "2024-01-01")
    val description: String?,
    val transactionType: String,                    // "Income", "Expense", "Transfer"

    val accountFromId: Int?,                        // Nullable for income
    val accountToId: Int?,                          // Nullable for expenses
    val categoryId: Int?,                           // Nullable for income and transfers
    val subcategoryId: Int?,                        // Nullable for income and transfers

    val createdAt: String = getCurrentTimestamp(),
    val lastUpdated: String = getCurrentTimestamp()
)
