package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.model.DashboardData
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError

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