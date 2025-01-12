package com.xmichxl.walletmanapp.core.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.ui.graphics.Color
import co.yml.charts.ui.piechart.models.PieChartData
import com.xmichxl.walletmanapp.features.analytics.data.CategoryAnalytics
import com.xmichxl.walletmanapp.ui.theme.CColorBlack
import com.xmichxl.walletmanapp.ui.theme.CColorBlue
import com.xmichxl.walletmanapp.ui.theme.CColorGold
import com.xmichxl.walletmanapp.ui.theme.CColorGray
import com.xmichxl.walletmanapp.ui.theme.CColorGreen
import com.xmichxl.walletmanapp.ui.theme.CColorMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorNavyBlue
import com.xmichxl.walletmanapp.ui.theme.CColorOrange
import com.xmichxl.walletmanapp.ui.theme.CColorRed
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryBlack
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryBlue
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryGold
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryGray
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryGreen
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryNavyBlue
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryOrange
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryRed
import com.xmichxl.walletmanapp.ui.theme.CColorSecondarySilver
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryTeal
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryYellow
import com.xmichxl.walletmanapp.ui.theme.CColorSilver
import com.xmichxl.walletmanapp.ui.theme.CColorTeal
import com.xmichxl.walletmanapp.ui.theme.CColorYellow
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone.getTimeZone

fun getColorsFromString(colorName: String): Pair<Color, Color> {
    return when (colorName) {
        "Yellow" -> Pair(CColorYellow, CColorSecondaryYellow) // Primary and secondary
        "Black" -> Pair(CColorBlack, CColorSecondaryBlack) // Contrast with white
        "Red" -> Pair(CColorRed, CColorSecondaryRed)
        "Orange" -> Pair(CColorOrange, CColorSecondaryOrange)
        "Blue" -> Pair(CColorBlue, CColorSecondaryBlue)
        "Green" -> Pair(CColorGreen, CColorSecondaryGreen)
        "Gray" -> Pair(CColorGray, CColorSecondaryGray)
        "Magenta" -> Pair(CColorMagenta, CColorSecondaryMagenta)
        "Silver" -> Pair(CColorSilver, CColorSecondarySilver)
        "Teal" -> Pair(CColorTeal, CColorSecondaryTeal)
        "NavyBlue" -> Pair(CColorNavyBlue, CColorSecondaryNavyBlue)
        "Gold" -> Pair(CColorGold, CColorSecondaryGold)
        else -> Pair(CColorGray, CColorSecondaryGray) // Default colors
    }
}

fun getIconFromString(transactionType: String, iconName: String): Int {
    return when {
        // First check for known category icons by iconName
        iconName == "food" -> AppIcons.Categories.Food
        iconName == "shopping" -> AppIcons.Categories.Shopping
        iconName == "house" -> AppIcons.Categories.Housing
        iconName == "transport" -> AppIcons.Categories.Transportation
        iconName == "vehicle" -> AppIcons.Categories.Vehicle
        iconName == "entertainment" -> AppIcons.Categories.Life
        iconName == "communication" -> AppIcons.Categories.Communication
        iconName == "bank" -> AppIcons.Categories.Financial
        iconName == "income" -> AppIcons.Categories.Income
        iconName == "other" -> AppIcons.Categories.Other

        // Fallback to transaction type if iconName is not matched
        transactionType == TransactionType.INCOME.value -> AppIcons.Transaction.Income
        transactionType == TransactionType.EXPENSE.value -> AppIcons.Transaction.Expense
        transactionType == TransactionType.TRANSFER.value -> AppIcons.Transaction.Transfer
        transactionType == TransactionType.ADJUSTMENT.value -> AppIcons.Transaction.Adjustment
        transactionType == TransactionType.PAYMENT.value -> AppIcons.Transaction.Payment

        // Default icon if no matches
        else -> AppIcons.Categories.Other
    }
}

fun mapCategoryAnalyticsToSlices(
    analytics: List<CategoryAnalytics>,
    getColorsFromString: (String) -> Pair<Color, Color>
): List<PieChartData.Slice> {
    return analytics.map { category ->
        val (primaryColor, _) = getColorsFromString(category.categoryColor)
        PieChartData.Slice(
            label = category.categoryName,
            value = category.total.toFloat(),
            color = primaryColor
        )
    }
}


// Stores the time in ISO 8601 (eg. 2024-12-11T12:30:00.000Z) UTC (Universal time)
// Remember: Convert to user's timezone if we want to show this value to the user.
fun getCurrentTimestamp(): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.now().toString()
    } else {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = getTimeZone("UTC")
        sdf.format(Date())
    }
}

/**
 * Returns the current date and time in ISO format "yyyy-MM-dd HH:mm"
 */
fun getCurrentDateTimeIso(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

/**
 * Returns the current date and time in "dd-MM-yyyy HH:mm" format
 */
fun getCurrentDateTimeLocal(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

/**
 * Returns the start and end date for the given time range.
 * Dates are formatted as "yyyy-MM-dd HH:mm".
 */
@SuppressLint("NewApi")
fun getDateRangeFor(timeRange: String): Pair<String, String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val today = LocalDate.now()

    return when (timeRange) {
        "today" -> {
            val startOfDay = today.atStartOfDay().format(formatter)
            val endOfDay = today.atTime(23, 59).format(formatter)
            startOfDay to endOfDay
        }
        "yesterday" -> {
            val yesterday = today.minusDays(1)
            val startOfDay = yesterday.atStartOfDay().format(formatter)
            val endOfDay = yesterday.atTime(23, 59).format(formatter)
            startOfDay to endOfDay
        }
        "lastWeek" -> {
            val startOfWeek = today.minusDays(7).atStartOfDay().format(formatter)
            val endOfWeek = today.atTime(23, 59).format(formatter)
            startOfWeek to endOfWeek
        }
        "currentMonth" -> {
            val startOfMonth = today.withDayOfMonth(1).atStartOfDay().format(formatter)
            val endOfMonth = today.atTime(23, 59).format(formatter)
            startOfMonth to endOfMonth
        }
        "lastMonth" -> {
            val lastMonth = today.minusMonths(1)
            val startOfLastMonth = lastMonth.withDayOfMonth(1).atStartOfDay().format(formatter)
            val endOfLastMonth = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()).atTime(23, 59).format(formatter)
            startOfLastMonth to endOfLastMonth
        }
        else -> throw IllegalArgumentException("Invalid time range: $timeRange")
    }
}

fun formatMoney(balance: Double): String {
    val formattedNumber = String.format("%,.2f", balance) // Add commas and two decimals
    return "$$formattedNumber" // Add the $ symbol
}

fun Double?.formatMoney(): String {
    return this?.let {
        val formattedNumber = String.format("%,.2f", it) // Add commas and two decimals
        "$$formattedNumber" // Add the $ symbol
    } ?: "$0.00" // Default value for null
}

/**
 * Formats a nullable Double value into a String.
 * - If the Double is a whole number (e.g., 5.0), the decimals are removed.
 * - If the Double has a fractional part, it is returned as is.
 * - If the value is null, an empty string is returned.
 *
 * @param value The Double value to format.
 * @return A String of the value without decimals if it's a whole number, or as a normal decimal string.
 */
fun formatDouble(value: Double?): String {
    return value?.takeIf { it % 1.0 == 0.0 }?.toInt()?.toString() ?: value?.toString() ?: ""
}
