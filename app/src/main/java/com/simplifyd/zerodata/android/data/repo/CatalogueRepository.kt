package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError
import zerodata_api.ApiRpc
import zerodata_api.EdgeGrpc

class CatalogueRepository: BaseRepository() {

    fun getListedApps(): Status<List<ApiRpc.ListedApps.ListedApp>>{
        return try {
            val catalogueRq = ApiRpc.Empty.newBuilder().build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val response = blockingStub.getListedApps(catalogueRq)

            Status.Success(response.listedAppsList)
        }catch (error: Throwable){
            Status.Error(handleError(error))
        }
    }
}