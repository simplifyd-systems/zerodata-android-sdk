package com.simplifyd.zerodata.data.repo

import com.simplifyd.zerodata.data.local.PreferenceManager
import com.simplifyd.zerodata.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.utils.Status
import com.simplifyd.zerodata.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import zerodata_api.ApiRpc
import zerodata_api.EdgeGrpc

@ExperimentalCoroutinesApi
class InitializationRepository: BaseRepository() {

    suspend fun initialize(packageName: String, user_id: String): Status<Unit>{
        return try {
            val initializeRq = ApiRpc.Initialize.newBuilder().setPackageName(packageName).setAppUserId(user_id).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.initializeSDK(initializeRq)

            saveToken(response.jwt)
            Status.Success(Unit)
        } catch (error: Throwable) {
            error.printStackTrace()
            Status.Error(handleError(error))
        }
    }

    private suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            PreferenceManager.saveToken(token)
        }
    }
}