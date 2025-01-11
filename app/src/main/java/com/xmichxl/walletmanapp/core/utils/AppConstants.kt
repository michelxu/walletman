package com.xmichxl.walletmanapp.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import com.xmichxl.walletmanapp.R
import com.xmichxl.walletmanapp.ui.theme.CColorBlack
import com.xmichxl.walletmanapp.ui.theme.CColorBlue
import com.xmichxl.walletmanapp.ui.theme.CColorGold
import com.xmichxl.walletmanapp.ui.theme.CColorGray
import com.xmichxl.walletmanapp.ui.theme.CColorGreen
import com.xmichxl.walletmanapp.ui.theme.CColorMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorNavyBlue
import com.xmichxl.walletmanapp.ui.theme.CColorOrange
import com.xmichxl.walletmanapp.ui.theme.CColorRed
import com.xmichxl.walletmanapp.ui.theme.CColorSilver
import com.xmichxl.walletmanapp.ui.theme.CColorTeal
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
    ADJUSTMENT("Adjustment"),
    PAYMENT("Payment")
}


// FORM CONSTANTS
// Grouped constants inside the object
object AppConstants {
    val accountTypes = listOf("Debit Card", "Credit Card", "Cash", "Other")

    val transactionTypes = listOf("Expense", "Income", "Transfer", "Payment")
    //val transactionTypesFilter = listOf("Expense", "Income", "Transfer", "Payment", "Adjustment", "All")
    //val dateRangesFilter = listOf("today", "yesterday", "lastWeek", "currentMonth", "lastMonth", "All")
    val dateRangesFilter = mapOf(
        "today" to "Today",
        "yesterday" to "Yesterday",
        "lastWeek" to "Last Week",
        "currentMonth" to "Current Month",
        "lastMonth" to "Last Month",
        "All" to "All"
    )
    val transactionTypesFilter = mapOf(
        "Expense" to "Expense",
        "Income" to "Income",
        "Transfer" to "Transfer",
        "Payment" to "Payment",
        "Adjustment" to "Adjustment",
        "All" to "All"
    )

    val colors = listOf(
        "Red" to CColorRed,
        "Orange" to CColorOrange,
        "Blue" to CColorBlue,
        "Magenta" to CColorMagenta,
        "Green" to CColorGreen,
        "Yellow" to CColorYellow,
        "Black" to CColorBlack,
        "Gray" to CColorGray,
        "Silver" to CColorSilver,
        "Teal" to CColorTeal,
        "NavyBlue" to CColorNavyBlue,
        "Gold" to CColorGold
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
        val BarChart = R.drawable.bar_chart
        val Search = Icons.Default.Search
        val Walletman = R.drawable.bar_chart
    }

    object Transaction {
        val Income = R.drawable.income
        val Expense = R.drawable.expense
        val Transfer = R.drawable.transfer
        val Adjustment = R.drawable.adjustment
        val Payment = R.drawable.expense
    }

    object Categories {
        val Food = R.drawable.category_food
        val Shopping = R.drawable.category_shopping
        val Housing = R.drawable.category_housing
        val Transportation = R.drawable.category_transportation
        val Vehicle = R.drawable.category_vehicle
        val Life = R.drawable.category_life
        val Communication = R.drawable.category_communication
        val Financial = R.drawable.money
        val Income = R.drawable.income
        val Other = R.drawable.category_other
    }

    object PaymentMethod {
        val card = R.drawable.card
        val cash = R.drawable.cash
    }
}