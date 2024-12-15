package com.xmichxl.walletmanapp.features.account.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xmichxl.walletmanapp.core.utils.getCurrentTimestamp

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val status: Int = 1,                // 1 Active 2 Inactive
    val number: String?,                //Last 4 digits (Nullable for non-cards accounts)
    val type: String,                   // "Cash", "Credit Card", "Debit Card", "Other"
    val balance: Double,
    val creditLimit: Int?,              // Nullable for non-credit accounts

    val color: String = "Gray",   // For the UI customization
    val icon: String? = "",             // Path or icon name

    val createdAt: String = getCurrentTimestamp(),
    val lastUpdated: String = getCurrentTimestamp()
)
