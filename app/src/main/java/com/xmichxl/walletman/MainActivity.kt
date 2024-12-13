package com.xmichxl.walletman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xmichxl.walletman.core.navigation.NavManager
import com.xmichxl.walletman.ui.theme.WalletManTheme
import com.xmichxl.walletman.features.account.viewmodels.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val accountViewModel: AccountViewModel by viewModels()

        setContent {
            WalletManTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavManager(
                        modifier = Modifier.padding(innerPadding),
                        accountViewModel
                    )
                }
            }
        }
    }
}






@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}