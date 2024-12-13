package com.xmichxl.walletman.core.utils

import android.os.Build
import androidx.compose.ui.graphics.Color
import com.xmichxl.walletman.ui.theme.CColorBlack
import com.xmichxl.walletman.ui.theme.CColorBlue
import com.xmichxl.walletman.ui.theme.CColorGray
import com.xmichxl.walletman.ui.theme.CColorGreen
import com.xmichxl.walletman.ui.theme.CColorMagenta
import com.xmichxl.walletman.ui.theme.CColorRed
import com.xmichxl.walletman.ui.theme.CColorSecondaryBlack
import com.xmichxl.walletman.ui.theme.CColorSecondaryBlue
import com.xmichxl.walletman.ui.theme.CColorSecondaryGray
import com.xmichxl.walletman.ui.theme.CColorSecondaryGreen
import com.xmichxl.walletman.ui.theme.CColorSecondaryMagenta
import com.xmichxl.walletman.ui.theme.CColorSecondaryRed
import com.xmichxl.walletman.ui.theme.CColorSecondaryYellow
import com.xmichxl.walletman.ui.theme.CColorYellow
import java.time.Instant

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

fun validateForm(
    name: String,
    balance: String,
    accountType: String,
    creditLimit: String,
    onNameError: (String?) -> Unit,
    onBalanceError: (String?) -> Unit,
    onAccountTypeError: (String?) -> Unit,
    onCreditLimitError: (String?) -> Unit
): Boolean {
    var isValid = true

    // Name validation
    if (name.isBlank()) {
        onNameError("Name cannot be empty")
        isValid = false
    } else {
        onNameError(null)
    }

    // Balance validation
    if (balance.isBlank() || balance.toDoubleOrNull() == null) {
        onBalanceError("Enter a valid balance")
        isValid = false
    } else {
        onBalanceError(null)
    }

    // Account Type validation
    if (accountType.isBlank()) {
        onAccountTypeError("Select an account type")
        isValid = false
    } else {
        onAccountTypeError(null)
    }

    // Credit Limit validation
    if (creditLimit.isBlank()) {
        onCreditLimitError("Type a Credit Limit")
        isValid = false
    } else {
        onCreditLimitError(null)
    }

    return isValid
}

