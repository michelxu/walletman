package com.xmichxl.walletmanapp.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import com.xmichxl.walletmanapp.R
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
// Account form
const val FORM_ERROR_NAME = "Title cannot be empty"
const val FORM_ERROR_ACCOUNT = "Account type cannot be empty"
const val FORM_ERROR_BALANCE = "Balance cannot be empty"
const val FORM_ERROR_CREDIT_LIMIT = "Credit limit cannot be empty"

// Transaction form
const val FORM_ERROR_AMOUNT = "Amount cannot be empty"
const val FORM_ERROR_TRANSACTION = "Transaction cannot be empty"
const val FORM_ERROR_DESCRIPTION = "Description cannot be empty"
const val FORM_ERROR_DATE = "Date cannot be empty"
const val FORM_ERROR_ACCOUNT_FROM = "Account From type cannot be empty"
const val FORM_ERROR_ACCOUNT_TO = "Account To type cannot be empty"
const val FORM_ERROR_CATEGORY = "Category type cannot be empty"
const val FORM_ERROR_SUBCATEGORY = "Subcategory type cannot be empty"


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

enum class TransactionType(val value: String) {
    EXPENSE("Expense"),
    INCOME("Income"),
    TRANSFER("Transfer"),
    ADJUSTMENT("Adjustment")
}


// FORM CONSTANTS
// Grouped constants inside the object
object AppConstants {
    val accountTypes = listOf("Debit Card", "Credit Card", "Cash", "Other")

    val transactionTypes = listOf("Expense", "Income", "Transfer")

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


object AppIcons {
    object Main {
        val Home = Icons.Default.Home
        val Transaction = R.drawable.money
        val Account = R.drawable.wallet
        val Delete = Icons.Default.Delete
        val Back = Icons.AutoMirrored.Filled.ArrowBack
        val Add = Icons.Default.Add

    }

    object Transaction {
        val Income = R.drawable.income
        val Expense = R.drawable.expense
        val Transfer = R.drawable.transfer
        val Adjustment = R.drawable.adjustment
    }

    object PaymentMethod {
        val card = R.drawable.card
        val cash = R.drawable.cash
    }

    object Categories {
        val Entertainment = Icons.Default.FavoriteBorder
    }
}