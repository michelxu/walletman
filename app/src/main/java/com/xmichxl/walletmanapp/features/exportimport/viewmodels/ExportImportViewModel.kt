package com.xmichxl.walletmanapp.features.exportimport.viewmodels

import android.app.Application
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

    // Export data to JSON file
    fun exportData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val jsonData = repository.exportData()
            // Access the application context via getApplication()
            getApplication<Application>().applicationContext.contentResolver
                .openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonData.toByteArray())
            }
        }
    }

    // Import data from JSON file
    fun importData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            // Access the application context via getApplication()
            getApplication<Application>().applicationContext.contentResolver
                .openInputStream(uri)?.use { inputStream ->
                val jsonData = inputStream.bufferedReader().use { it.readText() }
                repository.importData(jsonData)
            }
        }
    }
}