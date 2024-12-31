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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.BottomNavigationBar
import com.xmichxl.walletmanapp.core.components.FloatButton
import com.xmichxl.walletmanapp.core.components.LastTransactions
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHomeView(
    navController: NavController,
    transactionViewModel: TransactionViewModel
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
        ContentTransactionHomeView(it, navController, transactionViewModel)
    }
}

@Composable
fun ContentTransactionHomeView(
    it: PaddingValues,
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    val transactionListWithDetails by transactionViewModel.transactionsWithDetails.collectAsState()

    LaunchedEffect(Unit) {
        transactionViewModel.getTransactionsWithDetails()
    }
    Log.d("transactions home", transactionListWithDetails.toString())

    Column(modifier = Modifier.padding(it)) {
        LastTransactions(transactionListWithDetails, navController)
    }
}