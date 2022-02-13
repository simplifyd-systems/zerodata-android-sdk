package com.simplifydvpn.android.ui.main.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.model.DashboardData
import com.simplifydvpn.android.data.repo.CredentialsRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OverviewViewModel : ViewModel() {

    private val credentialsRepository = CredentialsRepository()
    val getDashboardDataStatus = MutableLiveData<Status<DashboardData>>()
    val connectProfileStatus = MutableLiveData<Status<String>>()

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
}