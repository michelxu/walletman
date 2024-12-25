package com.xmichxl.walletmanapp.features.transaction.data

import androidx.room.Embedded
import androidx.room.Relation
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory

data class TransactionWithDetails(
    @Embedded
    val details: Transaction,
    @Relation(parentColumn = "accountFromId", entityColumn = "id")
    val accountFrom: Account?,
    @Relation(parentColumn = "accountToId", entityColumn = "id")
    val accountTo: Account?,
    @Relation(parentColumn = "subcategoryId", entityColumn = "id")
    val subcategory: Subcategory?,
    @Relation(parentColumn = "categoryId", entityColumn = "id")
    val category: Category?
)
