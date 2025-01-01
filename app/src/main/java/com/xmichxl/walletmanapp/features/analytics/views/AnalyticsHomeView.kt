package com.xmichxl.walletmanapp.features.analytics.views

import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.xmichxl.walletmanapp.core.components.BottomNavigationBar
import com.xmichxl.walletmanapp.core.components.MainIconButton
import com.xmichxl.walletmanapp.core.components.MainTitle
import com.xmichxl.walletmanapp.features.analytics.viewmodels.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsHomeView(
    navController: NavController,
    analyticsViewModel: AnalyticsViewModel
    ){
    var selectedItemBottomBar by remember { mutableIntStateOf(2) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { MainTitle(title = "Analytics") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            navigationIcon = {
                MainIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack) {
                    navController.popBackStack()
                }
            })
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItemBottomBar,
                onItemSelected = { selectedItemBottomBar = it },
                navController = navController
            )
        }
    ) {
        ContentAnalyticsHomeView(it, analyticsViewModel)
    }
}


@Composable
fun ContentAnalyticsHomeView(
    it: PaddingValues,
    analyticsViewModel: AnalyticsViewModel
) {
    val data = DataUtils.getDonutChartData()
    val context = LocalContext.current

    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("HP", 15f, Color(0xFF5F0A87)),
            PieChartData.Slice("Dell", 30f, Color(0xFF20BF55)),
            PieChartData.Slice("Lenovo", 10f, Color(0xFFA40606)),
            PieChartData.Slice("Asus", 15f, Color(0xFFF53844)),
            PieChartData.Slice("Acer", 10f, Color(0xFFEC9F05)),
            PieChartData.Slice("Apple", 30f, Color(0xFF009FFD)),
        ),
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

    Column(modifier = Modifier.padding(it)) {
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = data, 3))

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