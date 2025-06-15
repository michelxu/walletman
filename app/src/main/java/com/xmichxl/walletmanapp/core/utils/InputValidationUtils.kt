package com.xmichxl.walletmanapp.core.utils

fun validateInput(
    input: String,
    maxLength: Int = Int.MAX_VALUE,
): String {
    var result = input
    result = result.replace("\n", "") // Remove newline characters
    result.trim() //Remove extra spaces
    if (result.length > maxLength) result = result.substring(0, maxLength) // Limit lenght
    return result
}


fun filterDecimals(input: String): String {
    return input.filterIndexed { index, char ->
        char.isDigit() ||
                (char == '.' && !input.substring(0, index).contains('.')) ||
                (char == '-' && index == 0) // Allow negative sign only at the beginning
    }
}