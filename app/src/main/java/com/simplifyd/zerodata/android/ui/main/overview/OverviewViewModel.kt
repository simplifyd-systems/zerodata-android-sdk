package com.simplifyd.zerodata.android.ui.main.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.model.DashboardData
import com.simplifyd.zerodata.android.data.repo.CredentialsRepository
import com.simplifyd.zerodata.android.data.repo.SettingsRepository
import com.simplifyd.zerodata.android.utils.SingleLiveData
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel : ViewModel() {

    private val credentialsRepository = CredentialsRepository()
    val getDashboardDataStatus = MutableLiveData<Status<DashboardData>>()
    val connectProfileStatus = SingleLiveData<Status<String>>()
    val checkPartnerNetwork = SingleLiveData<Status<Unit>>()
    private val settingsRepository = SettingsRepository()


    val connectUrl: String?
        get() {
            return (connectProfileStatus.value as? Status.Success<String>)?.data
        }


    fun connect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = credentialsRepository.getConnectProfile()
                print(result)
                connectProfileStatus.postValue(result)
            }
        }
    }

    fun checkIsPartnerNewtwork(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = credentialsRepository.getServiceAvailablity()
                print(result)
                checkPartnerNetwork.postValue(result)
            }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }
}