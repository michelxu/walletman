package com.xmichxl.walletmanapp.features.analytics.data

data class CategoryAnalytics(
    val categoryId: Int,
    val categoryName: String,
    val categoryColor: String,
    val categoryIcon: String,
    val total: Double,
    val transactionsCount: Int,
    val highestExpense: Double,
    val averagePerTransaction: Double,
    val percentageOfTotal: Double
)