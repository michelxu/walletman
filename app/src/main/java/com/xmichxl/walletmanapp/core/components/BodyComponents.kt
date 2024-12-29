package com.xmichxl.walletmanapp.core.components

import android.graphics.Typeface
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.xmichxl.walletmanapp.core.utils.AppIcons
import com.xmichxl.walletmanapp.core.utils.TransactionType
import com.xmichxl.walletmanapp.core.utils.formatMoney
import com.xmichxl.walletmanapp.core.utils.getColorsFromString
import com.xmichxl.walletmanapp.core.utils.getIconFromString
import com.xmichxl.walletmanapp.features.account.data.Account
import com.xmichxl.walletmanapp.features.analytics.data.CategoryAnalytics
import com.xmichxl.walletmanapp.features.transaction.data.TransactionWithDetails
import com.xmichxl.walletmanapp.features.transaction.utils.getAccountName
import com.xmichxl.walletmanapp.ui.theme.CColorGreen

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
fun ResumeHomeView(thisMonth: Double?, lastMonth: Double?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TotalSpentRow(label = "This Month", amount = thisMonth, isPrimary = true, isExpense = true)
        TotalSpentRow(label = "Last Month", amount = lastMonth, isPrimary = false, isExpense = true)
    }
}

@Composable
fun TotalSpentRow(label: String, amount: Double?, isPrimary: Boolean, isExpense: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isPrimary) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isPrimary) FontWeight.Medium else FontWeight.Normal,
            color = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
        Text(
            text = if(isExpense) "-${amount.formatMoney()}" else "+${amount.formatMoney()}",
            style = if (isPrimary) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
            fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal,
            color = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
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
        item {
            NewAccountCard(onClick = { navController.navigate("AccountAddView") })
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
fun NewAccountCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 300.dp, height = 180.dp)
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon inside a circle
                Box(
                    modifier = Modifier
                        .size(64.dp) // Size of the circle
                        .background(
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AppIcons.Main.Add,
                        contentDescription = "Add Account",
                        modifier = Modifier.size(32.dp), // Size of the icon
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "New Account",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}


@Composable
fun LastTransactions(transactions: List<TransactionWithDetails>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(
                transaction = transaction,
                onClick = { navController.navigate("TransactionEditView/${transaction.details.id}") }
            )
            //Spacer(modifier = Modifier.height(2.dp)) // Add spacing between items
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionWithDetails, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (primaryColor, secondaryColor) = getColorsFromString(transaction.category?.color ?: "Black")
        val icon = getIconFromString(transaction.category?.icon ?: "")

        // Rounded Shape with Icon (on the left)
        Box(
            modifier = Modifier
                .size(48.dp) // Size of the rounded square
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .background(MaterialTheme.colorScheme.primary), // Secondary color background
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Transaction Icon",
                modifier = Modifier.size(24.dp), // Icon size
                tint = Color.White // Icon color
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacing between the icon and content

        // Description and Details (center-aligned)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.details.description ?: "No name",
                style = MaterialTheme.typography.bodyLarge,
                color = if (transaction.details.type == TransactionType.INCOME.value)
                    CColorGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = transaction.getAccountName(),
                style = MaterialTheme.typography.bodySmall,
                color = if (transaction.details.type == TransactionType.INCOME.value)
                    CColorGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
            /*
            Text(
                text = transaction.category?.name ?: "-",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
             */
        }

        // Amount (on the right)
        Text(
            text = if (transaction.details.type == TransactionType.EXPENSE.value)
                "-${transaction.details.amount.formatMoney()}" else "+${transaction.details.amount.formatMoney()}",
            style = MaterialTheme.typography.bodyLarge,
            color = if (transaction.details.type == TransactionType.INCOME.value)
                CColorGreen else MaterialTheme.colorScheme.onSurface,
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
                navController.navigate("TransactionHomeView")
            }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = AppIcons.Main.BarChart), contentDescription = "Analytics") },
            label = { Text("Analytics") },
            selected = selectedItem == 2,
            onClick = {
                onItemSelected(2)
                navController.navigate("AnalyticsHomeView")
            }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = AppIcons.Main.Account), contentDescription = "Accounts") },
            label = { Text("Accounts") },
            selected = selectedItem == 3,
            onClick = {
                onItemSelected(3)
                navController.navigate("AccountAddView")
            }
        )
    }
}

@Composable
fun CategoryAnalyticsDonutChart(
    categoryAnalytics: List<CategoryAnalytics>,
    it: PaddingValues
) {
    val context = LocalContext.current

    if (categoryAnalytics.isEmpty()) {
        // Show a loading indicator or placeholder
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    } else {
        // Map the categoryAnalytics to PieChartData.Slice
        val slices = categoryAnalytics.map { analytics ->
            val (primaryColor, _) = getColorsFromString(analytics.categoryColor)
            PieChartData.Slice(
                label = analytics.categoryName,
                value = analytics.total.toFloat(),
                color = primaryColor
            )
        }

        // Create the chart data
        val donutChartData = PieChartData(
            slices = slices,
            plotType = PlotType.Donut
        )

        val donutChartConfig = PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = Color.Black,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 25,
            labelFontSize = 42.sp,
        )

        // Render the chart
        Column(modifier = Modifier.padding(it)) {
            Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = donutChartData, 3))

            DonutPieChart(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(500.dp),
                donutChartData,
                donutChartConfig
            ) { slice ->
                Toast.makeText(context, slice.label, Toast.LENGTH_SHORT).show()
            }
        }
    }
}



// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //DropdownTextField()
}