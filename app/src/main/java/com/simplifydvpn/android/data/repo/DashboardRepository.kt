package com.simplifydvpn.android.data.repo

import com.simplifydvpn.android.data.model.DashboardData
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError

class DashboardRepository : BaseRepository() {

    suspend fun getDashboardData(): Status<DashboardData> {
        return try {
            val dashboardData = apiService.getDashboardData()
            Status.Success(dashboardData)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }
}