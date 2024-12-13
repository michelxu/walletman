package com.xmichxl.walletman.core.views

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xmichxl.walletman.core.components.AccountsCarousel
import com.xmichxl.walletman.core.components.BodyTitleSection
import com.xmichxl.walletman.core.components.BottomNavigationBar
import com.xmichxl.walletman.core.components.FloatButton
import com.xmichxl.walletman.core.components.MainTitle
import com.xmichxl.walletman.features.account.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    modifier: Modifier,
    accountViewModel: AccountViewModel
) {
    var selectedItemBottomBar by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { MainTitle(title = "Home") }
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
                navController.navigate("AccountAddView")
            }
        }
    ) {
        ContentHomeView(it, navController, accountViewModel)
    }
}



@Composable
fun ContentHomeView(
    it: PaddingValues,
    navController: NavController,
    accountViewModel: AccountViewModel
) {
    val accountList by accountViewModel.accountList.collectAsState()

    Column(modifier = Modifier.padding(it)) {
        Spacer(modifier = Modifier.padding(top = 0.dp))
        AccountsCarousel(accountList, navController)

        BodyTitleSection(onClick = { /*TODO*/ }, title = "Transactions", onClickTitle = "View all")
    }
}