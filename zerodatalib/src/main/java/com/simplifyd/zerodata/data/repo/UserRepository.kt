package com.simplifyd.zerodata.data.repo

import android.util.Log
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
class UserRepository: BaseRepository() {

    suspend fun loginInitiate(mobile: String): Status<Unit> {
        return try {
            val loginInitiateRq = ApiRpc.Username.newBuilder().setMobile(mobile).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.initiateLogin(loginInitiateRq)

            saveToken(response.initiateLoginJwt)
            Status.Success(Unit)

        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun loginValidate(otp: String, app_version: String, platform: String): Status<Unit> {
        return try {
            val loginValidateRq =
                ApiRpc.LoginValidateRequest.newBuilder().setOtp(otp).setAppVersion(app_version)
                    .setPlatform(platform)
                    .setInitiateLoginJwt(PreferenceManager.getTokenInitation()).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.loginValidate(loginValidateRq)
            Log.d("loginValidate:", "Response Now Validate $response")
            Log.d("Token!!!", response.jwt)

            saveAuthToken(response.jwt)
            Status.Success(Unit)

        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    private suspend fun saveAuthToken(token: String) {
        withContext(Dispatchers.IO) {
            PreferenceManager.saveToken(token)
        }
    }

    private suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            PreferenceManager.saveTokenInitation(token)
        }
    }
}