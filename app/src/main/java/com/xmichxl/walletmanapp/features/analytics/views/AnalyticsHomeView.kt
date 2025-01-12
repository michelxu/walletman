package com.xmichxl.walletmanapp.features.analytics.views

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
import com.xmichxl.walletmanapp.core.components.CategoryList
import com.xmichxl.walletmanapp.core.components.CategorySummary
import com.xmichxl.walletmanapp.core.components.FilterAnalyticsRow
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.features.analytics.data.CategoryAnalytics
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel

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


@Composable
fun ContentAnalyticsHomeView(
    it: PaddingValues,
    navController: NavController,
    analyticsViewModel: AnalyticsViewModel,
    transactionViewModel: TransactionViewModel
) {
    val categoryAnalytics by analyticsViewModel.categoryAnalytics.collectAsState()
    val filteredTransactions by transactionViewModel.filteredTransactions.collectAsState()
    // FilterRow Selected
    val selectedDateRange = remember { mutableStateOf("Current Month") }
    var selectedCategory by remember { mutableStateOf<CategoryAnalytics?>(null) }
    var currentCategoryIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(Unit) {
        val initialFilter = mapOf("dateRange" to "currentMonth")

        analyticsViewModel.getCategoryAnalytics("currentMonth")
        transactionViewModel.applyFilters(initialFilter)
    }
    Log.d("Analytics home view", categoryAnalytics.toString())

    Column(modifier = Modifier.padding(it)) {
        FilterAnalyticsRow(selectedDateRange) { selectedFilters ->
            analyticsViewModel.getCategoryAnalytics(selectedFilters["dateRange"]!!)
            transactionViewModel.applyFilters(selectedFilters)
        }

        //LastTransactions(filteredTransactions, navController)

        selectedCategory?.let { category ->
            CategorySummary(category)
        }

        CategoryList(
            categoryAnalytics,
            onCategoryClick = { category ->
                // Handle click event here
                Log.d("Category Clicked", "Clicked on: ${category.categoryName}")
                // Update your other UI components based on the clicked category
                selectedCategory = category
            }
        )
    }
}