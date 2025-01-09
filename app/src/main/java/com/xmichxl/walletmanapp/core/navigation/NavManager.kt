package com.xmichxl.walletmanapp.core.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xmichxl.walletmanapp.core.views.HomeView
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.account.views.AccountAddView
import com.xmichxl.walletmanapp.features.account.views.AccountEditView
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel
import com.xmichxl.walletmanapp.features.analytics.views.AnalyticsHomeView
import com.xmichxl.walletmanapp.features.category.viewmodels.CategoryViewModel
import com.xmichxl.walletmanapp.features.exportimport.viewmodels.ExportImportViewModel
import com.xmichxl.walletmanapp.features.exportimport.views.ExportImportHomeView
import com.xmichxl.walletmanapp.features.subcategory.viewmodels.SubcategoryViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel
import com.xmichxl.walletmanapp.features.transaction.views.TransactionAddView
import com.xmichxl.walletmanapp.features.transaction.views.TransactionEditView
import com.xmichxl.walletmanapp.features.transaction.views.TransactionHomeView

@Composable
fun NavManager(
    modifier: Modifier,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel,
    subcategoryViewModel: SubcategoryViewModel,
    analyticsViewModel: AnalyticsViewModel,
    exportImportViewModel: ExportImportViewModel,
    onExportClick: (Intent) -> Unit,
    onImportClick: (Intent) -> Unit
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") {
            HomeView(navController, modifier, accountViewModel, transactionViewModel, analyticsViewModel)
        }

        // ************ ACCOUNT
        composable("AccountAddView") {
            AccountAddView(navController, modifier, accountViewModel)
        }
        composable("AccountEditView/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id") ?: 0
            AccountEditView(navController, modifier, accountViewModel, id)
        }

        // ************ TRANSACTION
        composable("TransactionHomeView") {
            TransactionHomeView(navController, transactionViewModel, accountViewModel)
        }
        composable("TransactionAddView") {
            TransactionAddView(navController, modifier, transactionViewModel, accountViewModel, categoryViewModel)
        }
        composable("TransactionEditView/{id}", arguments = listOf(
            navArgument("id") { type = NavType.LongType }
        )) {
            val id = it.arguments?.getLong("id") ?: 0
            TransactionEditView(navController, modifier, transactionViewModel, accountViewModel, id)
        }

        // ************ ANALYTICS
        composable("AnalyticsHomeView") {
            AnalyticsHomeView(navController, analyticsViewModel)
        }

        // ************ EXPORT IMPORT
        composable("ExportImportHomeView") {
            ExportImportHomeView(
                viewModel = exportImportViewModel,
                onExportClick = onExportClick,
                onImportClick = onImportClick
            )
        }
    }
}