package com.xmichxl.walletmanapp.features.exportimport.views

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xmichxl.walletmanapp.features.exportimport.viewmodels.ExportImportViewModel

@Composable
fun ExportImportHomeView(
    viewModel: ExportImportViewModel,
    onExportClick: (Intent) -> Unit,
    onImportClick: (Intent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onExportClick(viewModel.createExportIntent()) }) {
            Text("Export Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onImportClick(viewModel.createImportIntent()) }) {
            Text("Import Data")
        }
    }
}