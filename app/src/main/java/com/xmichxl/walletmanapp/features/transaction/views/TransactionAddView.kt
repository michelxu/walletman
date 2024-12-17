package com.xmichxl.walletmanapp.features.transaction.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.ButtonAtBottom
import com.xmichxl.walletmanapp.core.components.DatePickerTextField
import com.xmichxl.walletmanapp.core.components.DropdownTextField
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AccountType
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_FROM
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_TO
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_AMOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_BALANCE
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_CREDIT_LIMIT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DATE
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DESCRIPTION
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_NAME
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_TRANSACTION
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentDateTime
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAddView(
    navController: NavController,
    modifier: Modifier,
    transactionViewModel: TransactionViewModel
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { MainTitle(title = "New Transaction") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                MainIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack) {
                    navController.popBackStack()
                }
            })
    }) {
        ContentTransactionAddView(it, navController, transactionViewModel)
    }
}


@Composable
fun ContentTransactionAddView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    var type by remember { mutableStateOf("Expense") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(getCurrentDateTime()) }
    var accountFrom by remember { mutableStateOf("") }
    var accountTo by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val transactionTypes = AppConstants.transactionTypes
    val testMyAccounts = listOf("1", "2", "3")

    Column(modifier = Modifier.padding(it)) {
        // ********************* ACCOUNT TYPE
        DropdownTextField(
            value = type,
            onValueChange = { type = it },
            list = transactionTypes,
            label = "Transaction Type"
        )

        // ********************* DESCRIPTION
        MainTextField(
            value = description,
            onValueChange = {
                description = validateInput(it, maxLength = 60)
            },
            label = "Description",
            isErrorMsg = errorMessage == FORM_ERROR_DESCRIPTION
        )
        if (errorMessage == FORM_ERROR_DESCRIPTION) WarningFormText(title = FORM_ERROR_DESCRIPTION)

        // ********************* AMOUNT
        NumericTextField(
            value = amount,
            onValueChange = { amount = it },
            label = "Amount",
            maxLength = 9,
            allowDecimals = true,
            isErrorMsg = errorMessage == FORM_ERROR_AMOUNT
        )
        if (errorMessage == FORM_ERROR_AMOUNT) WarningFormText(title = FORM_ERROR_AMOUNT)

        // ********************* DATE
        DatePickerTextField(
            label = "Date",
            date = date,
            onValueChange = { newDate ->
                date = newDate
            }
        )

        // ********************* ACCOUNT FROM
        if (type == TransactionType.EXPENSE.value || type == TransactionType.TRANSFER.value)
        {
            DropdownTextField(
                value = accountFrom,
                onValueChange = {
                    accountFrom = it
                    if (type != TransactionType.TRANSFER.value) accountTo = ""
                },
                list = testMyAccounts,
                label = "Account From",
                isErrorMsg = errorMessage == FORM_ERROR_ACCOUNT_FROM
            )
            if (errorMessage == FORM_ERROR_ACCOUNT_FROM) WarningFormText(title = FORM_ERROR_ACCOUNT_FROM)
        }

        // ********************* ACCOUNT TO
        if (type == TransactionType.INCOME.value || type == TransactionType.TRANSFER.value)
        {
            DropdownTextField(
                value = accountTo,
                onValueChange = {
                    accountTo = it
                    if (type != TransactionType.TRANSFER.value) accountFrom = ""
                },
                list = testMyAccounts,
                label = "Account To",
                isErrorMsg = errorMessage == FORM_ERROR_ACCOUNT_TO
            )
            if (errorMessage == FORM_ERROR_ACCOUNT_TO) WarningFormText(title = FORM_ERROR_ACCOUNT_TO)
        }



        // ********************* ADD BUTTON
        ButtonAtBottom(
            onClick = {
                when {
                    // VALIDATE FIELDS
                    type == TransactionType.EXPENSE.value && accountFrom.isBlank() || type == TransactionType.TRANSFER.value && accountFrom.isBlank() -> {
                        errorMessage = FORM_ERROR_ACCOUNT_FROM
                    }
                    type == TransactionType.INCOME.value && accountTo.isBlank() || type == TransactionType.TRANSFER.value && accountTo.isBlank() -> {
                        errorMessage = FORM_ERROR_ACCOUNT_TO
                    }
                    description.isBlank() -> errorMessage = FORM_ERROR_DESCRIPTION
                    amount.isBlank() -> errorMessage = FORM_ERROR_AMOUNT
                    else -> {
                        transactionViewModel.addTransaction(
                            Transaction(
                                type = type,
                                description = description,
                                amount = amount.toDouble(),
                                date = date,
                                accountToId = if(accountTo.isNotBlank()) accountTo.toInt() else null,
                                accountFromId = if (accountFrom.isNotBlank()) accountFrom.toInt() else null
                            )
                        )
                        navController.popBackStack()
                    }
                }
            },
            title = "Add Transaction"
        )
    }
}