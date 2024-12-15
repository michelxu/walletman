package com.xmichxl.walletmanapp.core.utils

import com.xmichxl.walletmanapp.ui.theme.CColorBlack
import com.xmichxl.walletmanapp.ui.theme.CColorBlue
import com.xmichxl.walletmanapp.ui.theme.CColorGray
import com.xmichxl.walletmanapp.ui.theme.CColorGreen
import com.xmichxl.walletmanapp.ui.theme.CColorMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorRed
import com.xmichxl.walletmanapp.ui.theme.CColorYellow

// Global constants outside the object
const val ACCOUNT_TYPE_DEBIT = "Debit Card"
const val ACCOUNT_TYPE_CREDIT = "Credit Card"
const val ACCOUNT_TYPE_CASH = "Cash"
const val ACCOUNT_TYPE_OTHER = "Other"

// Form Errors
const val FORM_ERROR_NAME = "Title cannot be empty"
const val FORM_ERROR_ACCOUNT = "Account type cannot be empty"
const val FORM_ERROR_BALANCE = "Balance cannot be empty"
const val FORM_ERROR_CREDIT_LIMIT = "Credit limit cannot be empty"

enum class AccountStatus(val value: Int) {
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3)
}

enum class AccountType(val value: String) {
    DEBIT("Debit Card"),
    CREDIT("Credit Card"),
    CASH("Cash"),
    OTHER("Other")
}


// FORM CONSTANTS
// Grouped constants inside the object
object AppConstants {
    val accountTypes = listOf("Debit Card", "Credit Card", "Cash", "Other")
    val colors = listOf(
        "Red" to CColorRed,
        "Blue" to CColorBlue,
        "Magenta" to CColorMagenta,
        "Green" to CColorGreen,
        "Yellow" to CColorYellow,
        "Black" to CColorBlack,
        "Gray" to CColorGray
    )
}