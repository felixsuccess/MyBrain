package com.mhss.app.mybrain.presentation.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhss.app.mybrain.domain.model.preferences.PrefsKey
import com.mhss.app.mybrain.domain.use_case.settings.ExportAllDataUseCase
import com.mhss.app.mybrain.domain.use_case.settings.GetPreferenceUseCase
import com.mhss.app.mybrain.domain.use_case.settings.ImportAllDataUseCase
import com.mhss.app.mybrain.domain.use_case.settings.SavePreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val savePreference: SavePreferenceUseCase,
    private val getPreference: GetPreferenceUseCase,
    private val exportData: ExportAllDataUseCase,
    private val importData: ImportAllDataUseCase
) : ViewModel() {

    private val _backupResult = MutableStateFlow<BackupResult>(BackupResult.None)
    val backupResult: StateFlow<BackupResult> = _backupResult

    fun <T> getSettings(key: PrefsKey<T>, defaultValue: T): Flow<T> {
        return getPreference(key, defaultValue)
    }

    fun <T> saveSettings(key: PrefsKey<T>, value: T) {
        viewModelScope.launch {
            savePreference(key, value)
        }
    }

    fun importDatabase(
        uri: Uri,
        encrypted: Boolean,
        password: String
    ) {
        viewModelScope.launch {
            _backupResult.update { BackupResult.Loading }
            val result = importData(uri, encrypted, password)
            if (result) {
                _backupResult.update { BackupResult.ImportSuccess }
            } else {
                _backupResult.update { BackupResult.ImportFailed }
            }
        }
    }

    fun exportDatabase(
        uri: Uri,
        encrypted: Boolean,
        password: String
    ) {
        viewModelScope.launch {
            _backupResult.update { BackupResult.Loading }
            val result = exportData(uri, encrypted, password)
            if (result) {
                _backupResult.update { BackupResult.ExportSuccess }
            } else {
                _backupResult.update { BackupResult.ExportFailed }
            }
        }
    }

    sealed class BackupResult {
        data object ExportSuccess : BackupResult()
        data object ExportFailed : BackupResult()
        data object ImportSuccess : BackupResult()
        data object ImportFailed : BackupResult()
        data object Loading : BackupResult()
        data object None : BackupResult()
    }

}