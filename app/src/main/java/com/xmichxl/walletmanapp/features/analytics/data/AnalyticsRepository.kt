package com.xmichxl.walletmanapp.features.analytics.data

import com.xmichxl.walletmanapp.features.transaction.data.TransactionDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnalyticsRepository @Inject constructor(private val transactionDao: TransactionDao) {
    fun getCategoryAnalytics(startDate: String, endDate: String): Flow<List<CategoryAnalytics>> {
        return transactionDao.getCategoryAnalytics(startDate, endDate)
    }

    fun getDailySpendingTrend(startDate: String, endDate: String): Flow<List<DailySpendingTrend>> {
        return transactionDao.getDailySpendingTrend(startDate, endDate)
    }
}