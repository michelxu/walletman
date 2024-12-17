package com.xmichxl.walletmanapp.core.utils

import android.os.Build
import androidx.compose.ui.graphics.Color
import com.xmichxl.walletmanapp.ui.theme.CColorBlack
import com.xmichxl.walletmanapp.ui.theme.CColorBlue
import com.xmichxl.walletmanapp.ui.theme.CColorGray
import com.xmichxl.walletmanapp.ui.theme.CColorGreen
import com.xmichxl.walletmanapp.ui.theme.CColorMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorRed
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryBlack
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryBlue
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryGray
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryGreen
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryMagenta
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryRed
import com.xmichxl.walletmanapp.ui.theme.CColorSecondaryYellow
import com.xmichxl.walletmanapp.ui.theme.CColorYellow
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

fun getColorsFromString(colorName: String): Pair<Color, Color> {
    return when (colorName) {
        "Yellow" -> Pair(CColorYellow, CColorSecondaryYellow) // Primary and secondary
        "Black" -> Pair(CColorBlack, CColorSecondaryBlack) // Contrast with white
        "Red" -> Pair(CColorRed, CColorSecondaryRed)
        "Blue" -> Pair(CColorBlue, CColorSecondaryBlue)
        "Green" -> Pair(CColorGreen, CColorSecondaryGreen)
        "Gray" -> Pair(CColorGray, CColorSecondaryGray)
        "Magenta" -> Pair(CColorMagenta, CColorSecondaryMagenta)
        else -> Pair(CColorGray, CColorSecondaryGray) // Default colors
    }
}

// Stores the time in ISO 8601 (eg. 2024-12-11T12:30:00.000Z) UTC (Universal time)
// Remember: Convert to user's timezone if we want to show this value to the user.
fun getCurrentTimestamp(): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Instant.now().toString()
    } else {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        sdf.format(java.util.Date())
    }
}

// Returns the current date and time in "dd-MM-yyyy HH:mm" format (e.g., "15-04-2024 14:30")
fun getCurrentDateTime(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

fun formatMoney(balance: Double): String {
    val formattedNumber = String.format("%,.2f", balance) // Add commas and two decimals
    return "$$formattedNumber" // Add the $ symbol
}