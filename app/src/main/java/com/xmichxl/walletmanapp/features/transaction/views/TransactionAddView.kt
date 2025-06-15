package com.xmichxl.walletmanapp.features.transaction.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.NameIdDropdownTextField
import com.xmichxl.walletmanapp.core.components.ButtonAtBottom
import com.xmichxl.walletmanapp.core.components.DatePickerTextField
import com.xmichxl.walletmanapp.core.components.DropdownTextField
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_FROM
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT_TO
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_AMOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_CATEGORY
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DESCRIPTION
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_SUBCATEGORY
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentDateTimeIso
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.account.utils.getDisplayName
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.category.viewmodels.CategoryViewModel
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAddView(
    navController: NavController,
    modifier: Modifier,
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel,
    categoryViewModel: CategoryViewModel,
) {
    LaunchedEffect(Unit) {
        transactionViewModel.loadCategoriesIfNeeded()
        transactionViewModel.observeCategorySelectionIfNeeded()
        transactionViewModel.resetCategoryAndSubcategories()
    }

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
        ContentTransactionAddView(it, navController, transactionViewModel, accountViewModel, categoryViewModel)
    }
}


@Composable
fun ContentTransactionAddView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel,
    categoryViewModel: CategoryViewModel,
) {
    var type by remember { mutableStateOf("Expense") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(getCurrentDateTimeIso()) }
    var accountFromName by remember { mutableStateOf("") } //Store name to show in the DropDownTextBox
    var accountFromId by remember { mutableStateOf("") } // Store id to save in the database
    var accountToName by remember { mutableStateOf("") }
    var accountToId by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var subcategoryName by remember { mutableStateOf("") }
    var subcategoryId by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Collect accounts from AccountViewModel
    val accountList by accountViewModel.accountList.collectAsState()
    val categoryList by transactionViewModel.categoryList.collectAsState()
    val subcategoryList by transactionViewModel.subcategoryList.collectAsState()
    val transactionTypes = AppConstants.transactionTypes

    Log.d("categoryList", categoryList.toString())
    Log.d("subcategoryList", subcategoryList.toString())

    Column(modifier = Modifier.padding(it)) {
        // ********************* ACCOUNT TYPE
        DropdownTextField(
            value = type,
            onValueChange = {
                type = it
                if (type == TransactionType.TRANSFER.value) {
                    categoryName = ""
                    categoryId = ""
                    subcategoryName = ""
                    subcategoryId = ""
                }
            },
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

        // ********************* CATEGORY
        if (type == TransactionType.EXPENSE.value || type == TransactionType.INCOME.value)
        {
            NameIdDropdownTextField(
                value = categoryName,
                onValueChange = { name, id ->
                    categoryName = name
                    categoryId = id.toString()
                    subcategoryName = ""
                    subcategoryId = ""
                    transactionViewModel.setSelectedCategoryId(id!!)
                },
                list = categoryList.map { it.name to it.id }, // Convert Account to Pair<String, Int>
                label = "Category",
                isErrorMsg = errorMessage == FORM_ERROR_CATEGORY
            )
            if (errorMessage == FORM_ERROR_CATEGORY) WarningFormText(title = FORM_ERROR_CATEGORY)
        }

        // ********************* SUBCATEGORY
        if (type == TransactionType.EXPENSE.value || type == TransactionType.INCOME.value)
        {
            NameIdDropdownTextField(
                value = subcategoryName,
                onValueChange = { name, id ->
                    subcategoryName = name
                    subcategoryId = id.toString()
                },
                list = subcategoryList.map { it.name to it.id }, // Convert Account to Pair<String, Int>
                label = "Subcategory",
                isErrorMsg = errorMessage == FORM_ERROR_SUBCATEGORY
            )
            if (errorMessage == FORM_ERROR_SUBCATEGORY) WarningFormText(title = FORM_ERROR_SUBCATEGORY)
        }

        // ********************* DATE
        DatePickerTextField(
            label = "Date",
            date = date,
            onValueChange = { newDate ->
                date = newDate
            }
        )

        // ********************* ACCOUNT FROM
        if (type == TransactionType.EXPENSE.value || type == TransactionType.TRANSFER.value || type == TransactionType.PAYMENT.value)
        {
            NameIdDropdownTextField(
                value = accountFromName,
                onValueChange = { name, id ->
                    accountFromName = name
                    accountFromId = id.toString()
                    if (type != TransactionType.TRANSFER.value && type != TransactionType.PAYMENT.value)
                        accountToName = ""
                },
                list = accountList.map { it.getDisplayName() to it.id }, // Convert Account to Pair<String, Int>
                label = "Account From",
                isErrorMsg = errorMessage == FORM_ERROR_ACCOUNT_FROM
            )
            if (errorMessage == FORM_ERROR_ACCOUNT_FROM) WarningFormText(title = FORM_ERROR_ACCOUNT_FROM)
        }

        // ********************* ACCOUNT TO
        if (type == TransactionType.INCOME.value || type == TransactionType.TRANSFER.value || type == TransactionType.PAYMENT.value)
        {
            NameIdDropdownTextField(
                value = accountToName,
                onValueChange = { name, id ->
                    accountToName = name
                    accountToId = id.toString()
                    if (type != TransactionType.TRANSFER.value && type != TransactionType.PAYMENT.value)
                        accountFromName = ""
                },
                list = accountList.map { it.getDisplayName() to it.id }, // Convert Account to Pair<String, Int>
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
                    type == TransactionType.EXPENSE.value && accountFromName.isBlank() ||
                            type == TransactionType.TRANSFER.value && accountFromName.isBlank() ||
                            type == TransactionType.PAYMENT.value && accountFromName.isBlank() -> {
                        errorMessage = FORM_ERROR_ACCOUNT_FROM
                    }
                    type == TransactionType.INCOME.value && accountToName.isBlank() ||
                            type == TransactionType.TRANSFER.value && accountToName.isBlank() ||
                            type == TransactionType.PAYMENT.value && accountToName.isBlank() -> {
                        errorMessage = FORM_ERROR_ACCOUNT_TO
                    }
                    type == TransactionType.EXPENSE.value && categoryName.isBlank() || type == TransactionType.INCOME.value && categoryName.isBlank() -> {
                        errorMessage = FORM_ERROR_CATEGORY
                    }
                    type == TransactionType.EXPENSE.value && subcategoryName.isBlank() || type == TransactionType.INCOME.value && subcategoryName.isBlank() -> {
                        errorMessage = FORM_ERROR_SUBCATEGORY
                    }
                    description.isBlank() -> errorMessage = FORM_ERROR_DESCRIPTION
                    amount.isBlank() -> errorMessage = FORM_ERROR_AMOUNT
                    else -> {
                        val newTransaction = Transaction(
                            type = type,
                            description = description,
                            amount = amount.toDouble(),
                            date = date,
                            accountToId = if(accountToName.isNotBlank()) accountToId.toInt() else null,
                            accountFromId = if (accountFromName.isNotBlank()) accountFromId.toInt() else null,
                            categoryId = if(categoryName.isNotBlank()) categoryId.toInt() else null,
                            subcategoryId = if(subcategoryName.isNotBlank()) subcategoryId.toInt() else null
                        )

                        transactionViewModel.createTransactionAndUpdateBalance(newTransaction)
                        navController.popBackStack()
                    }
                }
            },
            title = "Add Transaction"
        )
    }
}