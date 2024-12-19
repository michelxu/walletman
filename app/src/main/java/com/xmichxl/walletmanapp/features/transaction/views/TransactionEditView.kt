package com.xmichxl.walletmanapp.features.transaction.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.ButtonAtBottom
import com.xmichxl.walletmanapp.core.components.DatePickerTextField
import com.xmichxl.walletmanapp.core.components.DropdownTextField
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_FROM
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_TO
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_AMOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DATE
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DESCRIPTION
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_TRANSACTION
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentTimestamp
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditView(
    navController: NavController,
    modifier: Modifier,
    transactionViewModel: TransactionViewModel,
    id: Long
) {
    // Observe the selected account
    val selectedTransaction by transactionViewModel.selectedTransaction.collectAsState()

    // Trigger account loading when the screen is opened
    LaunchedEffect(Unit) {
        transactionViewModel.getTransactionById(id)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Edit Transaction") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    MainIconButton(icon = AppIcons.Main.Back) {
                        navController.popBackStack()
                    }
                },
                actions = {
                    // Only show delete button if selectedAccount is not null
                    selectedTransaction?.let {
                        IconButton(onClick = {
                            transactionViewModel.deleteTransactionAndUpdateBalance(it)
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = AppIcons.Main.Delete,
                                contentDescription = "Delete Transaction",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) {
        ContentEditAddView(it, navController, transactionViewModel, id, selectedTransaction)
    }
}


@Composable
fun ContentEditAddView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    id: Long,
    selectedTransaction: Transaction?
) {
    if (selectedTransaction == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Use derived state for fields
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var accountFrom by remember { mutableStateOf("") }
    var accountTo by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val transactionTypes = AppConstants.transactionTypes
    val testMyAccounts = listOf("1", "2", "3")

    // Update state whenever selectedAccount changes
    LaunchedEffect(selectedTransaction) {
        type = selectedTransaction.type
        description = selectedTransaction.description
        amount = selectedTransaction.amount.toString()
        date = selectedTransaction.date
        accountFrom = selectedTransaction.accountFromId?.toString() ?: "" // Safely convert or default to empty string
        accountTo = selectedTransaction.accountToId?.toString() ?: ""     // Safely convert or default to empty string
    }

    Column(modifier = Modifier.padding(it)) {
        // ********************* ACCOUNT TYPE
        MainTextField(
            value = type,
            onValueChange = { type = it },
            label = "Transaction Type",
            isEnabled = false
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
            label = "Select Date",
            date = date,
            onValueChange = { newDate ->
                date = newDate
            }
        )

        // ********************* ACCOUNT FROM
        if (type == TransactionType.EXPENSE.value || type == TransactionType.TRANSFER.value)
        {
            MainTextField(
                value = accountFrom,
                onValueChange = { accountFrom = it },
                label = "Account From",
                isEnabled = false
            )
        }

        // ********************* ACCOUNT TO
        if (type == TransactionType.INCOME.value || type == TransactionType.TRANSFER.value)
        {
            MainTextField(
                value = accountTo,
                onValueChange = { accountTo = it },
                label = "Account To",
                isEnabled = false
            )
        }

        // ********************* ADD BUTTON
        ButtonAtBottom(
            onClick = {
                Log.d("Update transaction", selectedTransaction.toString())
                when {
                    // VALIDATE FIELDS
                    description.isBlank() -> errorMessage = FORM_ERROR_DESCRIPTION
                    amount.isBlank() -> errorMessage = FORM_ERROR_AMOUNT
                    else -> {
                        val newTransaction = Transaction(
                            id = id,
                            type = type,
                            description = description,
                            amount = amount.toDouble(),
                            date = date,
                            accountToId = if(accountTo.isNotBlank()) accountTo.toInt() else null,
                            accountFromId = if (accountFrom.isNotBlank()) accountFrom.toInt() else null,
                            lastUpdated = getCurrentTimestamp()
                        )

                        transactionViewModel.updateTransactionAndUpdateBalance(newTransaction)
                        navController.popBackStack()
                    }
                }
            },
            title = "Update Transaction"
        )
    }
}