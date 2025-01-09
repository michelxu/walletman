package com.xmichxl.walletmanapp.features.exportimport.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xmichxl.walletmanapp.features.shared.data.AccountTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportImportViewModel @Inject constructor(
    private val repository: AccountTransactionRepository,
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // Create IntentSender for Export & Import
    fun createExportIntent(): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "walletman_data.json")
        }
    }
    fun createImportIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
    }

    // Export data to JSON file
    fun exportData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonData = repository.exportData()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonData.toByteArray())
            }
        }
    }
    // Import data from JSON file
    fun importData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val jsonData = inputStream.bufferedReader().use { it.readText() }
                repository.importData(jsonData)
            }
        }
    }
}