package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.data.model.DashboardData
import com.simplifyd.zerodata.android.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError
import zerodata_api.ApiRpc
import zerodata_api.EdgeGrpc

class DashboardRepository : BaseRepository() {

    suspend fun getDashboardData(): Status<DashboardData> {
        return try {
            val dashboardData = apiService.getDashboardData()
            Status.Success(dashboardData)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    fun getDataSaved(connectionTime: Long): Status<Long>{
        return try {
            val dataSavedRq = ApiRpc.TimeRange.newBuilder().setSinceMs(connectionTime).build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val response = blockingStub.getUsedDataBytes(dataSavedRq)
            Status.Success( response.totalByteCount)

        }catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }
}