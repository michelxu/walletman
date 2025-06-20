package com.xmichxl.walletmanapp.core.components

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        val icon = getIconFromString(
            transactionType = transaction.details.type,
            iconName = transaction.category?.icon ?: ""
        )

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
                maxLines = 1,
                overflow =  TextOverflow.Ellipsis,
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
        NavigationBarItem(
            icon = { Icon(imageVector = AppIcons.Main.Back, contentDescription = "Export Import") },
            label = { Text("Other") },
            selected = selectedItem == 4,
            onClick = {
                onItemSelected(4)
                navController.navigate("ExportImportHomeView")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), // Apply padding to the whole column
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No data")
        }
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
        val donutChartData = remember(categoryAnalytics) {
            PieChartData(
                slices = slices,
                plotType = PlotType.Donut
            )
        }

        val donutChartConfig = PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.background,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 25,
            labelFontSize = 42.sp,
        )

        // Render the chart
        Column() {
            Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = donutChartData, 3))

            DonutPieChart(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    //.fillMaxWidth()
                    .width(500.dp)
                    .height(350.dp),
                donutChartData,
                donutChartConfig
            ) { slice ->
                Log.d("donut", slice.label)
                //Toast.makeText(context, slice.label, Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
fun CategoryList(
    categories: List<CategoryAnalytics>,
    onCategoryClick: (CategoryAnalytics) -> Unit
) {
    // Sort the categories by total amount spent in descending order
    val sortedCategories = categories.sortedByDescending { it.total }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 items per row
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sortedCategories) { category ->
            CategoryBox(category = category, onClick = { onCategoryClick(category) })
        }
    }
}

@Composable
fun CategoryBox(
    category: CategoryAnalytics,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f) // Ensures the box is a square
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Icon(
                painter = painterResource(getIconFromString("", category.categoryIcon)),
                contentDescription = category.categoryName,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Name
            Text(
                text = category.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Total Amount
            Text(
                text = "${category.total}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (category.total > 500) Color.Red else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CategorySummary(category: CategoryAnalytics) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Main box for name and total spent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon inside a square box with rounded corners
                Box(
                    modifier = Modifier
                        .size(48.dp) // Size of the square box
                        .clip(RoundedCornerShape(8.dp)) // Rounded corners
                        //.background(MaterialTheme.colorScheme.secondaryContainer) // Secondary color background
                        .padding(8.dp), // Padding inside the box to fit the icon nicely
                    contentAlignment = Alignment.Center
                ) {
                    // Display category icon inside the box
                    Icon(
                        painter = painterResource(id = getIconFromString("", category.categoryIcon)), // Use the icon based on the category
                        contentDescription = category.categoryName,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Column for the title and total spent
                Column {
                    // Title - Category Name
                    Text(
                        text = category.categoryName,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Total Spent
                    Text(
                        text = formatMoney(category.total),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        // Grid of smaller boxes
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Transactions count
            item {
                SummaryBox(
                    label = "Transactions",
                    value = category.transactionsCount.toString()
                )
            }
            // Highest expense
            item {
                SummaryBox(
                    label = "Highest Expense",
                    value = "$${category.highestExpense.toInt()}"
                )
            }
            // Average per transaction
            item {
                SummaryBox(
                    label = "Avg/Transaction",
                    value = "$${category.averagePerTransaction.toInt()}"
                )
            }
            // Percentage of total spending
            item {
                SummaryBox(
                    label = "% of Total",
                    value = "${category.percentageOfTotal.toInt()} %"
                )
            }
        }
    }
}

@Composable
fun SummaryBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategorySummaryPager(
    categories: List<CategoryAnalytics>,
    pagerState: PagerState,
    onCategorySelected: (String) -> Unit // Returns the selected categoryId
) {
    // Track the current category ID
    // remember(categories) adding categories as key refreshes the data and fixes the bug
    val selectedCategoryId = remember(categories) {
        derivedStateOf { categories[pagerState.currentPage].categoryId }
    }

    // Notify parent composable about the selected category
    LaunchedEffect(pagerState.currentPage) {
        val selectedCategory = if (pagerState.currentPage == 0) "All" else selectedCategoryId.value.toString()
        onCategorySelected(selectedCategory)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Horizontal Pager for Categories
        HorizontalPager(
            state = pagerState,
            //modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { page ->
            val category = categories[page]
            CategorySummary(category)
        }

        // Custom Pager Indicator
        CustomPagerIndicator(
            pagerState = pagerState,
            pageCount = categories.size,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.Gray,
    indicatorSize: Dp = 8.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (page in 0 until pageCount) {
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .clip(CircleShape)
                    .background(if (pagerState.currentPage == page) activeColor else inactiveColor)
            )
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