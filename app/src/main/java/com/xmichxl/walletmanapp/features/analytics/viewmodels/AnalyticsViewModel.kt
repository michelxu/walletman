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

    private val _categoryAnalytics = MutableStateFlow<List<CategoryAnalytics>>(emptyList())
    val categoryAnalytics = _categoryAnalytics.asStateFlow()

    private val _dailySpendingTrend = MutableStateFlow<List<DailySpendingTrend>>(emptyList())
    val dailySpendingTrend = _dailySpendingTrend.asStateFlow()

    fun loadCategoryAnalytics(timeRange: String) {
        viewModelScope.launch {
            val (startDate, endDate) = getDateRangeFor(timeRange)

            analyticsRepository.getCategoryAnalytics(startDate, endDate).collect { data ->
                _categoryAnalytics.value = data
            }
        }
    }

    fun loadDailySpendingTrend(startDate: String, endDate: String) {
        viewModelScope.launch {
            analyticsRepository.getDailySpendingTrend(startDate, endDate).collect { data ->
                _dailySpendingTrend.value = data
            }
        }
    }
}