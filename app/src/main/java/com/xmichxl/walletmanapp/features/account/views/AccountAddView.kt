package com.xmichxl.walletmanapp.features.account.views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.ButtonAtBottom
import com.xmichxl.walletmanapp.core.components.ColorPickerDropdown
import com.xmichxl.walletmanapp.core.components.DropdownTextField
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTextField
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.NumericTextField
import com.xmichxl.walletmanapp.core.components.WarningFormText
import com.xmichxl.walletmanapp.core.utils.AccountType
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_ACCOUNT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_BALANCE
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_CREDIT_LIMIT
import com.xmichxl.walletmanapp.core.utils.FORM_ERROR_NAME
import com.xmichxl.walletmanapp.core.utils.validateInput
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountAddView(
    navController: NavController,
    modifier: Modifier,
    accountViewModel: AccountViewModel
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { MainTitle(title = "New Account") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                MainIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack) {
                    navController.popBackStack()
                }
            })
    }) {
        ContentAccountAddView(it, navController, accountViewModel)
    }
}

@Composable
fun ContentAccountAddView(
    it: PaddingValues,
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    Column(modifier = Modifier.padding(it)) {
        var name by remember { mutableStateOf("") }
        var number by remember { mutableStateOf("") }
        var balance by remember { mutableStateOf("") }
        var creditLimit by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }
        var selectedColor by remember { mutableStateOf("Gray") }

        var errorMessage by remember { mutableStateOf<String?>(null) }

        val accountTypes = AppConstants.accountTypes
        val colors = AppConstants.colors

        // ********************* ACCOUNT TYPE
        DropdownTextField(
            value = type,
            onValueChange = { type = it },
            list = accountTypes,
            label = "Account Type",
            isErrorMsg = errorMessage == FORM_ERROR_ACCOUNT
        )
        if (errorMessage == FORM_ERROR_ACCOUNT) WarningFormText(title = FORM_ERROR_ACCOUNT)

        // ********************* TITLE
        MainTextField(
            value = name,
            onValueChange = {
                name = validateInput(it, maxLength = 30)
            },
            label = "Title",
            isErrorMsg = errorMessage == FORM_ERROR_NAME
        )
        if (errorMessage == FORM_ERROR_NAME) WarningFormText(title = FORM_ERROR_NAME)


        // ********************* CARD NUMBER
        if (type == AccountType.CREDIT.value || type == AccountType.DEBIT.value){
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
            label = if(type == AccountType.CREDIT.value) "Credit Available" else "Balance",
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

        // ********************* ADD BUTTON
        ButtonAtBottom(
            onClick = {
                when {
                    type.isBlank() -> errorMessage = FORM_ERROR_ACCOUNT
                    name.isBlank() -> errorMessage = FORM_ERROR_NAME
                    balance.isBlank() -> errorMessage = FORM_ERROR_BALANCE
                    type == AccountType.CREDIT.value && creditLimit.isBlank() -> {
                        errorMessage = FORM_ERROR_CREDIT_LIMIT
                    }

                    else -> {
                        /*
                        accountViewModel.addAccount(
                            Account(
                                name = name,
                                number = number,
                                type = type,
                                balance = balance.toDouble(),
                                creditLimit = creditLimit.toIntOrNull(),
                                color = selectedColor
                            )
                        )*/
                        accountViewModel.createAccountWithTransaction(
                            Account(
                                name = name,
                                number = number,
                                type = type,
                                balance = balance.toDouble(),
                                creditLimit = creditLimit.toIntOrNull(),
                                color = selectedColor
                            ),
                            balance.toDouble()
                        )
                        navController.popBackStack()
                    }
                }
            },
            title = "Add New Account"
        )
    }
}