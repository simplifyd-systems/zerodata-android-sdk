package com.simplifyd.zerodata.android.ui.main.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.repo.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val settingsRepository = SettingsRepository()

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }
}