package com.xmichxl.walletmanapp.features.account.utils

import com.xmichxl.walletmanapp.features.account.data.Account

fun Account.getDisplayName(): String {
    return "[${id}] $name ${number ?: ""}".trim()
}
