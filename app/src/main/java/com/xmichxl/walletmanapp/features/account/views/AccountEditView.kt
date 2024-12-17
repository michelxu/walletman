package com.xmichxl.walletmanapp.features.account.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.ButtonAtBottom
import com.xmichxl.walletmanapp.core.components.ColorPickerDropdown
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AccountType
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_BALANCE
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_CREDIT_LIMIT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_NAME
import com.xmichxl.walletmanapp.core.utils.getCurrentTimestamp
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditView(
    navController: NavController,
    modifier: Modifier,
    accountViewModel: AccountViewModel,
    id: Int
) {
    // Observe the selected account
    val selectedAccount by accountViewModel.selectedAccount.collectAsState()

    // Trigger account loading when the screen is opened
    LaunchedEffect(Unit) {
        accountViewModel.getAccountById(id)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Edit Account") },
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
                    selectedAccount?.let {
                        IconButton(onClick = {
                            accountViewModel.deleteAccount(it)
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = AppIcons.Main.Delete,
                                contentDescription = "Delete Account",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) {
        ContentAccountEditView(it, navController, accountViewModel, id, selectedAccount)
    }
}



@Composable
fun ContentAccountEditView(
    it: PaddingValues,
    navController: NavController,
    accountViewModel: AccountViewModel,
    id: Int,
    selectedAccount: Account?
) {
    if (selectedAccount == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Use derived state for fields
    var name by remember { mutableStateOf(selectedAccount.name) }
    var number by remember { mutableStateOf(selectedAccount.number ?: "") }
    var balance by remember { mutableStateOf(selectedAccount.balance.toString()) }
    var creditLimit by remember { mutableStateOf(selectedAccount.creditLimit?.toString() ?: "") } // Safely convert or default to empty string
    var type by remember { mutableStateOf(selectedAccount.type) }
    var selectedColor by remember { mutableStateOf(selectedAccount.color) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val colors = AppConstants.colors

    // Update state whenever selectedAccount changes
    LaunchedEffect(selectedAccount) {
        name = selectedAccount.name
        number = selectedAccount.number ?: ""
        balance = selectedAccount.balance.toString()
        creditLimit = selectedAccount.creditLimit?.toString() ?: ""
        type = selectedAccount.type
        selectedColor = selectedAccount.color
    }

    Column(modifier = Modifier.padding(it)) {
        Log.d("AccountEditView", "${selectedAccount?.name} ${selectedAccount?.id.toString()}")
        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        // ********************* ACCOUNT TYPE
        MainTextField(
            value = type,
            onValueChange = { type = it },
            label = "Account Type",
            isEnabled = false
        )

        // ********************* TITLE
        MainTextField(
            value = name,
            onValueChange = { name = validateInput(it, maxLength = 30) },
            label = "Title",
            isErrorMsg = errorMessage == FORM_ERROR_NAME

        )
        if (errorMessage == FORM_ERROR_NAME) WarningFormText(title = FORM_ERROR_NAME)


        // ********************* CARD NUMBER
        if (type == AccountType.CREDIT.value || type == AccountType.DEBIT.value) {
            NumericTextField(
                value = number,
                onValueChange = { number = it },
                label = "Last 4 digits",
                maxLength = 4,
            )
        }

        // ********************* BALANCE
        NumericTextField(
            value = balance,
            onValueChange = { balance = it },
            label = if (type == AccountType.CREDIT.value) "Credit Available" else "Balance",
            maxLength = 9,
            allowDecimals = true,
            isErrorMsg = errorMessage == FORM_ERROR_BALANCE
        )
        if (errorMessage == FORM_ERROR_BALANCE) WarningFormText(title = FORM_ERROR_BALANCE)

        // ********************* CREDIT LIMIT
        if (type == AccountType.CREDIT.value) {
            NumericTextField(
                value = creditLimit,
                onValueChange = { creditLimit = it },
                label = "Credit Limit",
                maxLength = 9,
                isErrorMsg = errorMessage == FORM_ERROR_CREDIT_LIMIT
            )
        }
        if (errorMessage == FORM_ERROR_CREDIT_LIMIT) WarningFormText(title = FORM_ERROR_CREDIT_LIMIT)

        // ********************* COLOR
        ColorPickerDropdown(
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it },
            colors = colors,
            label = "Color"
        )

        // ********************* UPDATE BUTTON
        ButtonAtBottom(
            onClick = {
                Log.d("updatebutton", "$selectedAccount")

                when {
                    name.isBlank() -> errorMessage = FORM_ERROR_NAME
                    balance.isBlank() -> errorMessage = FORM_ERROR_BALANCE
                    type == AccountType.CREDIT.value && creditLimit.isBlank() -> {
                        errorMessage = FORM_ERROR_CREDIT_LIMIT
                    } else -> {
                    accountViewModel.updateAccount(
                        Account(
                            id = id,
                            name = name,
                            number = number,
                            type = type,
                            balance = balance.toDouble(),
                            creditLimit = creditLimit.toIntOrNull(),
                            color = selectedColor,
                            lastUpdated = getCurrentTimestamp()
                        )
                    )
                    navController.popBackStack()
                }
                }
            },
            title = "Update Account"
        )
    }
}