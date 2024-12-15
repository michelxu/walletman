package com.xmichxl.walletmanapp.core.navigation

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

@Composable
fun NavManager(modifier: Modifier, accountViewModel: AccountViewModel){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home"){
        composable("Home"){
            HomeView(navController, modifier, accountViewModel)
        }
        composable("AccountAddView"){
            AccountAddView(navController, modifier, accountViewModel)
        }
        composable("AccountEditView/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType}
        )){
            val id = it.arguments?.getInt("id") ?: 0
            AccountEditView(navController, modifier, accountViewModel, id)
        }
    }
}