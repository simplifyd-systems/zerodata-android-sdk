package com.simplifydvpn.android.ui.main.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.model.DashboardData
import com.simplifydvpn.android.data.repo.DashboardRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    private val dashboardRepository = DashboardRepository()

    val getDashboardDataStatus = MutableLiveData<Status<DashboardData>>()

    init {
        getDashboardData()
    }

    fun getDashboardData() {
        getDashboardDataStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = dashboardRepository.getDashboardData()
            getDashboardDataStatus.postValue(result)
        }

    }
}