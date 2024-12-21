package com.xmichxl.walletmanapp.features.transaction.data

import androidx.room.Embedded
import androidx.room.Relation
import com.xmichxl.walletmanapp.features.account.data.Account

data class TransactionWithAccounts(
    @Embedded val details: Transaction,
    @Relation(parentColumn = "accountFromId", entityColumn = "id")
    val accountFrom: Account?,
    @Relation(parentColumn = "accountToId", entityColumn = "id")
    val accountTo: Account?
)
