package com.xmichxl.walletmanapp.features.transaction.utils

import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithAccounts
import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithDetails

fun TransactionWithAccounts.getAccountName(): String {
    return accountFrom?.name ?: accountTo?.name ?: "Unknown"
}

fun TransactionWithDetails.getAccountName(): String {
    return accountFrom?.name ?: accountTo?.name ?: "Unknown"
}
