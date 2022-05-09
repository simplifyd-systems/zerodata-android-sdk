package com.simplifyd.zerodata.android.ui.main.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.repo.SettingsRepository
import kotlinx.coroutines.launch

class MoreSharedViewModel : ViewModel() {

    private val settingsRepository = SettingsRepository()

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }
}