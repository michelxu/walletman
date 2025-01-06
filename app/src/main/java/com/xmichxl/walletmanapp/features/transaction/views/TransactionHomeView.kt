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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.BottomNavigationBar
import com.xmichxl.walletmanapp.core.components.FilterRow
import com.xmichxl.walletmanapp.core.components.FloatButton
import com.xmichxl.walletmanapp.core.components.LastTransactions
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHomeView(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel
    ) {
    var selectedItemBottomBar by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
        CenterAlignedTopAppBar(title = { MainTitle(title = "Transactions") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                MainIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack) {
                    navController.popBackStack()
                }
            })
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItemBottomBar,
                onItemSelected = { selectedItemBottomBar = it },
                navController = navController
            )
        },
        floatingActionButton = {
            FloatButton {
                navController.navigate("TransactionAddView")
            }
        }
    ) {
        ContentTransactionHomeView(it, navController, transactionViewModel, accountViewModel)
    }
}

@Composable
fun ContentTransactionHomeView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    accountViewModel: AccountViewModel
) {
    val transactionListWithDetails by transactionViewModel.transactionsWithDetails.collectAsState()
    val filteredTransactions by transactionViewModel.filteredTransactions.collectAsState()
    val categoryList by transactionViewModel.categoryList.collectAsState()
    val accountList by accountViewModel.accountList.collectAsState()
    var filters by remember { mutableStateOf(emptyMap<String, String>()) }
    val dateRanges = AppConstants.dateRangesFilter
    var timeRange by remember { mutableStateOf("currentMonth") }

    // FilterRow Selected
    val selectedDateRange = remember { mutableStateOf("All") }
    val selectedType = remember { mutableStateOf("All") }
    val selectedAccount = remember { mutableStateOf("All") }
    val selectedCategory = remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        transactionViewModel.loadCategoriesIfNeeded()
        transactionViewModel.getTransactionsWithDetails()
    }
    Log.d("transactions home", transactionListWithDetails.toString())
    Log.d("filteredTransactions", filteredTransactions.toString())
    Log.d("categoryList", categoryList.toString())


    Column(modifier = Modifier.padding(it)) {
        // Call to FilterRow composable
        FilterRow(
            selectedDateRange = selectedDateRange,
            selectedType = selectedType,
            selectedCategory = selectedCategory,
            selectedAccount = selectedAccount,
            categoryList = categoryList,
            accountList = accountList
        ) { selectedFilters ->
            // Handle the filters once they are applied
            transactionViewModel.applyFilters(selectedFilters)
        }
        LastTransactions(filteredTransactions, navController)
    }
}