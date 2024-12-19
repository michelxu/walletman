package com.xmichxl.walletmanapp.core.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xmichxl.walletmanapp.core.utils.AppConstants
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.formatMoney
import com.xmichxl.walletmanapp.core.utils.getColorsFromString
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.transaction.data.Transaction

@Composable
fun MainTitle(title: String) {
    Text(text = title, color = Color.White, fontWeight = FontWeight.Bold)
}

@Composable
fun BodyTitleSection(
    onClick: () -> Unit,
    title: String,
    onClickTitle: String,
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .padding(15.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f) // Pushes the second element to the right
        )

        Text(
            text = onClickTitle,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
fun AccountsCarousel(accounts: List<Account>, navController: NavController) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(accounts) { account ->
            val (primaryColor, secondaryColor) = getColorsFromString(account.color)

            AccountCard(
                onClick = { navController.navigate("AccountEditView/${account.id}") },
                id = account.id,
                accountName = account.name,
                number = account.number,
                balance = account.balance ?: 0.0,
                accountType = account.type,
                gradientColors = listOf(primaryColor, secondaryColor) // Example gradient
            )
        }
    }
}

@Composable
fun AccountCard(
    onClick: () -> Unit,
    id: Int,
    accountName: String,
    number: String?,
    balance: Double,
    accountType: String,
    gradientColors: List<Color>,
) {
    Card(
        modifier = Modifier
            .size(width = 300.dp, height = 180.dp)
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),

        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradientColors))
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = "$accountName [$id]",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatMoney(balance),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Text(
                    text = if (!number.isNullOrEmpty()) "**** **** **** $number" else "",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = accountType,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LastTransactions(transactions: List<Transaction>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                onClick = { navController.navigate("TransactionEditView/${transaction.id}") })
            //Spacer(modifier = Modifier.height(2.dp)) // Add spacing between items
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rounded Shape with Icon (on the left)
        Box(
            modifier = Modifier
                .size(48.dp) // Size of the rounded square
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .background(MaterialTheme.colorScheme.primary), // Secondary color background
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = when(transaction.type) {
                    TransactionType.EXPENSE.value -> painterResource(AppIcons.Transaction.Expense)
                    TransactionType.INCOME.value -> painterResource(AppIcons.Transaction.Income)
                    TransactionType.TRANSFER.value -> painterResource(AppIcons.Transaction.Transfer)
                    else -> painterResource(AppIcons.Transaction.Adjustment)
                }, // Replace with relevant icon
                contentDescription = "Transaction Icon",
                modifier = Modifier.size(24.dp), // Icon size
                tint = Color.White // Icon color
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacing between the icon and content

        // Description and Details (center-aligned)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description ?: "No name",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Account",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Amount (on the right)
        Text(
            text = if (transaction.type == TransactionType.EXPENSE.value)
                "-$${transaction.amount}" else "+$${transaction.amount}",
            style = MaterialTheme.typography.bodyLarge,
            color = if (transaction.type == TransactionType.EXPENSE.value)
                MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavController,
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(AppIcons.Main.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = {
                onItemSelected(0)
                navController.navigate("Home")
            }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = AppIcons.Main.Transaction), contentDescription = "Transactions") },
            label = { Text("Transactions") },
            selected = selectedItem == 1,
            onClick = {
                onItemSelected(1)
                navController.navigate("TransactionAddView")
            }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = AppIcons.Main.Account), contentDescription = "Accounts") },
            label = { Text("Accounts") },
            selected = selectedItem == 2,
            onClick = {
                onItemSelected(2)
                navController.navigate("AccountAddView")
            }
        )
    }
}


// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //DropdownTextField()
}