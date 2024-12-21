package com.xmichxl.walletmanapp.features.transaction.utils

import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithAccounts

fun TransactionWithAccounts.getAccountName(): String {
    return accountFrom?.name ?: accountTo?.name ?: "Unknown"
}
