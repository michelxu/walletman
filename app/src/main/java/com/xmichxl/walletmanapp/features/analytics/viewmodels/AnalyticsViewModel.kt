package com.xmichxl.walletmanapp.features.analytics.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.core.utils.getDateRangeFor
import com.xmichxl.walletmanapp.features.analytics.data.AnalyticsRepository
import com.xmichxl.walletmanapp.features.analytics.data.CategoryAnalytics
import com.xmichxl.walletmanapp.features.analytics.data.DailySpendingTrend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(private val analyticsRepository: AnalyticsRepository): ViewModel() {

    private val _totalSpentCurrentMonth = MutableStateFlow<Double?>(null)
    val totalSpentCurrentMonth = _totalSpentCurrentMonth.asStateFlow()

    private val _totalSpentLastMonth = MutableStateFlow<Double?>(null)
    val totalSpentLastMonth = _totalSpentLastMonth.asStateFlow()

    private val _categoryAnalytics = MutableStateFlow<List<CategoryAnalytics>>(emptyList())
    val categoryAnalytics = _categoryAnalytics.asStateFlow()

    private val _dailySpendingTrend = MutableStateFlow<List<DailySpendingTrend>>(emptyList())
    val dailySpendingTrend = _dailySpendingTrend.asStateFlow()

    fun getTotalSpent(timeRange: String) {
        viewModelScope.launch {
            val (startDate, endDate) = getDateRangeFor(timeRange)

            analyticsRepository.getgetTotalSpent(startDate, endDate).collect { amount ->
                when(timeRange) {
                    "currentMonth" -> _totalSpentCurrentMonth.value = amount
                    "lastMonth" -> _totalSpentLastMonth.value = amount
                }
            }
        }
    }

    fun getCategoryAnalytics(timeRange: String) {
        viewModelScope.launch {
            val (startDate, endDate) = getDateRangeFor(timeRange)

            analyticsRepository.getCategoryAnalytics(startDate, endDate).collect { data ->
                _categoryAnalytics.value = data
            }
        }
    }

    fun getDailySpendingTrend(timeRange: String) {
        viewModelScope.launch {
            val (startDate, endDate) = getDateRangeFor(timeRange)

            analyticsRepository.getDailySpendingTrend(startDate, endDate).collect { data ->
                _dailySpendingTrend.value = data
            }
        }
    }
}