package com.xmichxl.walletmanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.xmichxl.walletmanapp.core.navigation.NavManager
import com.xmichxl.walletmanapp.features.account.viewmodels.AccountViewModel
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel
import com.xmichxl.walletmanapp.features.category.viewmodels.CategoryViewModel
import com.xmichxl.walletmanapp.features.subcategory.viewmodels.SubcategoryViewModel
import com.xmichxl.walletmanapp.features.transaction.viewmodels.TransactionViewModel
import com.xmichxl.walletmanapp.ui.theme.WalletmanappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val accountViewModel: AccountViewModel by viewModels()
        val transactionViewModel: TransactionViewModel by viewModels()
        val categoryViewModel: CategoryViewModel by viewModels()
        val subcategoryViewModel: SubcategoryViewModel by viewModels()
        val analyticsViewModel: AnalyticsViewModel by viewModels()

        setContent {
            WalletmanappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavManager(
                        modifier = Modifier.padding(innerPadding),
                        accountViewModel,
                        transactionViewModel,
                        categoryViewModel,
                        subcategoryViewModel,
                        analyticsViewModel
                    )
                }
            }
        }
    }
}