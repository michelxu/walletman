package com.xmichxl.walletmanapp.features.transaction.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NameIdDropdownTextField
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_AMOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_CATEGORY
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_DESCRIPTION
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_SUBCATEGORY
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.getCurrentTimestamp
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.utils.getDisplayName
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.category.data.Category
import com.xmichxl.walletmanapp.features.subcategory.data.Subcategory
import com.xmichxl.walletmanapp.features.transaction.data.Transaction
import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithDetails
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditView(
    navController: NavController,
    modifier: Modifier,
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel,
    id: Long,
) {
    // Observe the selected account
    val selectedTransaction by transactionViewModel.selectedTransaction.collectAsState()
    val selectedTransactionWithDetails by transactionViewModel.selectedTransactionWithDetails.collectAsState()
    val selectedAccountFrom by accountViewModel.selectedAccountFrom.collectAsState()
    val selectedAccountTo by accountViewModel.selectedAccountTo.collectAsState()
    val categoryList by transactionViewModel.categoryList.collectAsState()
    val subcategoryList by transactionViewModel.subcategoryList.collectAsState()

    // Trigger account loading when the screen is opened
    LaunchedEffect(Unit) {
        transactionViewModel.loadCategoriesIfNeeded()
        transactionViewModel.observeCategorySelectionIfNeeded()

        transactionViewModel.getTransactionById(id)
        transactionViewModel.getTransactionWithDetailsById(id)
    }

    // Trigger loading of the associated accounts
    LaunchedEffect(selectedTransactionWithDetails) {
        selectedTransactionWithDetails?.details?.accountFromId?.let { accountViewModel.getAccountFromById(it) }
        selectedTransactionWithDetails?.details?.accountToId?.let { accountViewModel.getAccountToById(it) }
    }

    // Determine if the data is loading
    val isLoading = selectedTransactionWithDetails == null ||
            (selectedTransactionWithDetails?.details?.accountFromId != null && selectedAccountFrom == null) ||
            (selectedTransactionWithDetails?.details?.accountToId != null && selectedAccountTo == null)

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
        if (isLoading) {
            // Show a loading indicator if the data is not ready
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Pass the data to the child composable
            ContentEditAddView(
                it,
                navController = navController,
                transactionViewModel = transactionViewModel,
                id = id,
                selectedTransactionWithDetails = selectedTransactionWithDetails,
                selectedAccountFrom = selectedAccountFrom,
                selectedAccountTo = selectedAccountTo,
                categoryList = categoryList,
                subcategoryList = subcategoryList
            )
        }
    }
}


@Composable
fun ContentEditAddView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    id: Long,
    selectedTransactionWithDetails: TransactionWithDetails?,
    selectedAccountFrom: Account?,
    selectedAccountTo: Account?,
    categoryList: List<Category>,
    subcategoryList: List<Subcategory>
) {
    // Use derived state for fields
    var typeState by remember { mutableStateOf("") }
    var descriptionState by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var accountFromState by remember { mutableStateOf("") } // Display the name of the related account
    var accountToState by remember { mutableStateOf("") }
    var accountFromStateId by remember { mutableStateOf("") } // Store the id of the related account
    var accountToStateId by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var subcategoryName by remember { mutableStateOf("") }
    var subcategoryId by remember { mutableStateOf("") }

    // Update state reactively when dependencies change
    LaunchedEffect(selectedTransactionWithDetails, selectedAccountFrom, selectedAccountTo) {
        typeState = selectedTransactionWithDetails?.details?.type ?: ""
        descriptionState = selectedTransactionWithDetails?.details?.description ?: ""
        amountState = selectedTransactionWithDetails?.details?.amount?.toString() ?: ""
        dateState = selectedTransactionWithDetails?.details?.date ?: ""
        accountFromState = selectedAccountFrom?.getDisplayName() ?: ""
        accountToState = selectedAccountTo?.getDisplayName() ?: ""
        accountFromStateId = selectedAccountFrom?.id.toString()
        accountToStateId = selectedAccountTo?.id.toString()
        categoryName = selectedTransactionWithDetails?.category?.name.toString()
        categoryId = selectedTransactionWithDetails?.category?.id.toString()
        subcategoryName = selectedTransactionWithDetails?.subcategory?.name.toString()
        subcategoryId = selectedTransactionWithDetails?.subcategory?.id.toString()

        selectedTransactionWithDetails?.category?.id?.let {
            transactionViewModel.setSelectedCategoryId(
                it
            )
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(it)) {
        // ********************* ACCOUNT TYPE
        MainTextField(
            value = typeState,
            onValueChange = { typeState = it },
            label = "Transaction Type",
            isEnabled = false
        )

        // ********************* DESCRIPTION
        MainTextField(
            value = descriptionState,
            onValueChange = {
                descriptionState = validateInput(it, maxLength = 60)
            },
            label = "Description",
            isErrorMsg = errorMessage == FORM_ERROR_DESCRIPTION
        )
        if (errorMessage == FORM_ERROR_DESCRIPTION) WarningFormText(title = FORM_ERROR_DESCRIPTION)

        // ********************* AMOUNT
        NumericTextField(
            value = amountState,
            onValueChange = { amountState = it },
            label = "Amount",
            maxLength = 9,
            allowDecimals = true,
            isErrorMsg = errorMessage == FORM_ERROR_AMOUNT
        )
        if (errorMessage == FORM_ERROR_AMOUNT) WarningFormText(title = FORM_ERROR_AMOUNT)

        // ********************* CATEGORY
        if (typeState == TransactionType.EXPENSE.value || typeState == TransactionType.INCOME.value)
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
        if (typeState == TransactionType.EXPENSE.value || typeState == TransactionType.INCOME.value)
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
            label = "Select Date",
            date = dateState,
            onValueChange = { newDate ->
                dateState = newDate
            }
        )

        // ********************* ACCOUNT FROM
        if (typeState == TransactionType.EXPENSE.value || typeState == TransactionType.TRANSFER.value) {
            MainTextField(
                value = accountFromState,
                onValueChange = { accountFromState = it },
                label = "Account From",
                isEnabled = false
            )
        }

        // ********************* ACCOUNT TO
        if (typeState == TransactionType.INCOME.value || typeState == TransactionType.TRANSFER.value) {
            MainTextField(
                value = accountToState,
                onValueChange = { accountToState = it },
                label = "Account To",
                isEnabled = false
            )
        }

        // ********************* ADD BUTTON
        ButtonAtBottom(
            onClick = {
                Log.d("Update transaction", descriptionState)
                when {
                    // VALIDATE FIELDS
                    descriptionState.isBlank() -> errorMessage = FORM_ERROR_DESCRIPTION
                    amountState.isBlank() -> errorMessage = FORM_ERROR_AMOUNT
                    typeState == TransactionType.EXPENSE.value && categoryName.isBlank() || typeState == TransactionType.INCOME.value && categoryName.isBlank() -> {
                        errorMessage = FORM_ERROR_CATEGORY
                    }
                    typeState == TransactionType.EXPENSE.value && subcategoryName.isBlank() || typeState == TransactionType.INCOME.value && subcategoryName.isBlank() -> {
                        errorMessage = FORM_ERROR_SUBCATEGORY
                    }
                    else -> {
                        val newTransaction = Transaction(
                            id = id,
                            type = typeState,
                            description = descriptionState,
                            amount = amountState.toDouble(),
                            date = dateState,
                            accountToId = if (accountToState.isNotBlank()) accountToStateId.toInt() else null,
                            accountFromId = if (accountFromState.isNotBlank()) accountFromStateId.toInt() else null,
                            categoryId = if(categoryName.isNotBlank()) categoryId.toInt() else null,
                            subcategoryId = if(subcategoryName.isNotBlank()) subcategoryId.toInt() else null,
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