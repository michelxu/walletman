package com.xmichxl.walletmanapp.features.analytics.views

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.components.BottomNavigationBar
import com.xmichxl.walletmanapp.core.components.CategorySummaryPager
import com.xmichxl.walletmanapp.core.components.FilterAnalyticsRow
import com.xmichxl.walletmanapp.core.components.LastTransactions
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsHomeView(
    navController: NavController,
    analyticsViewModel: AnalyticsViewModel,
    transactionViewModel: TransactionViewModel
){
    var selectedItemBottomBar by remember { mutableIntStateOf(2) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { MainTitle(title = "Analytics") },
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
        }
    ) {
        ContentAnalyticsHomeView(it, navController, analyticsViewModel, transactionViewModel)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentAnalyticsHomeView(
    it: PaddingValues,
    navController: NavController,
    analyticsViewModel: AnalyticsViewModel,
    transactionViewModel: TransactionViewModel
) {
    val categoryAnalytics by analyticsViewModel.categoryAnalytics.collectAsState()
    val filteredTransactions by transactionViewModel.filteredTransactions.collectAsState()

    // Create PagerState and CoroutineScope
    val pagerState = rememberPagerState(initialPage = 0) { categoryAnalytics.size }
    val coroutineScope = rememberCoroutineScope()

    // FilterRow Selected
    val selectedDateRangeOnScreen = remember { mutableStateOf("Current Month") }

    // Shared Filter State
    val selectedFilters = remember {
        mutableStateMapOf(
            "dateRange" to "currentMonth",
            "categoryId" to "All",
            "type" to "Expense"
        )
    }

    // Initial Filters on First Launch
    LaunchedEffect(Unit) {
        analyticsViewModel.getCategoryAnalytics("currentMonth")
        transactionViewModel.applyFilters(selectedFilters)
        // Reset Pager to First Page
        coroutineScope.launch {
            pagerState.scrollToPage(0)
        }
    }
    Log.d("Analytics home view", categoryAnalytics.toString())

    LaunchedEffect(categoryAnalytics) {
        coroutineScope.launch {
            pagerState.scrollToPage(0)
        }
    }

    // Layout
    Column(modifier = Modifier.padding(it)) {
        // Date Range Filter Row
        FilterAnalyticsRow(selectedDateRangeOnScreen) { newFilter ->
            val newDateRange = newFilter["dateRange"] ?: "currentMonth"
            selectedFilters["dateRange"] = newDateRange

            analyticsViewModel.getCategoryAnalytics(newDateRange)
            transactionViewModel.applyFilters(selectedFilters)

            // Reset Pager to First Page
            coroutineScope.launch {
                pagerState.scrollToPage(0)
            }
        }

        // Show Category Summary Pager if Analytics Data Exists
        if (categoryAnalytics.isNotEmpty()) {
            CategorySummaryPager(
                categories = categoryAnalytics,
                pagerState = pagerState,
                onCategorySelected = { categoryId ->
                    selectedFilters["categoryId"] = categoryId
                    transactionViewModel.applyFilters(selectedFilters)
                }
            )
        }

        // Show Filtered Transactions
        LastTransactions(filteredTransactions, navController)
    }
}