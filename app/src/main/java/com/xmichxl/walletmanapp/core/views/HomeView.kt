package com.xmichxl.walletmanapp.core.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.AccountsCarousel
import com.xmichxl.walletmanapp.core.components.BodyTitleSection
import com.xmichxl.walletmanapp.core.components.BottomNavigationBar
import com.xmichxl.walletmanapp.core.components.FloatButton
import com.xmichxl.walletmanapp.core.components.LastTransactions
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.core.components.ResumeHomeView
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    modifier: Modifier,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    analyticsViewModel: AnalyticsViewModel
) {
    var selectedItemBottomBar by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Home") },
                Modifier.padding(top = 0.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary)
            )
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
        ContentHomeView(it, navController, accountViewModel, transactionViewModel, analyticsViewModel)
    }
}


@Composable
fun ContentHomeView(
    it: PaddingValues,
    navController: NavController,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    analyticsViewModel: AnalyticsViewModel
) {
    val accountList by accountViewModel.accountList.collectAsState()
    val transactionListWithDetails by transactionViewModel.transactionsWithDetails.collectAsState()
    val transactionListByRange by transactionViewModel.transactionsByRange.collectAsState()
    val categoryAnalytics by analyticsViewModel.categoryAnalytics.collectAsState()
    val totalSpentCurrentMonth by analyticsViewModel.totalSpentCurrentMonth.collectAsState()
    val totalSpentLastMonth by analyticsViewModel.totalSpentLastMonth.collectAsState()

    LaunchedEffect(Unit){
        //analyticsViewModel.loadCategoryAnalytics("currentMonth")
        analyticsViewModel.getTotalSpent("currentMonth")
        analyticsViewModel.getTotalSpent("lastMonth")
    }

    //Log.d("Analytics bycat", categoryAnalytics.toString())

    Column(modifier = Modifier.padding(it)) {
        AccountsCarousel(accountList, navController)

        BodyTitleSection(onClick = { navController.navigate("AnalyticsHomeView") }, title = "Resume", onClickTitle = "View more")
        ResumeHomeView(totalSpentCurrentMonth, totalSpentLastMonth)

        BodyTitleSection(onClick = { navController.navigate("TransactionHomeView") }, title = "Last week", onClickTitle = "View more")
        LastTransactions(transactionListByRange, navController)
    }
}