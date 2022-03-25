package com.simplifyd.zerodata.android.data.repo

import android.util.Log
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.data.model.User
import com.simplifyd.zerodata.android.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import pb.ApiRpc
import pb.ApiRpc.LoginReq
import pb.ApiRpc.RegisterReq
import pb.EdgeGrpc


@ExperimentalCoroutinesApi
class UserRepository : BaseRepository() {

    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Status<Unit> {
        return try {
            val registerRequest =
                RegisterReq.newBuilder().setFname(firstName).setLname(lastName).setEmail(email)
                    .setMobile(phoneNumber).setPassword(password).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.register(registerRequest)
            if (response.success) {
                Log.d("REPOSITORY", "USER REPOSITORY: ${response}")
                Status.Success(Unit)
            } else {
                Log.d("REPOSITORY", "USER REPOSITORY: ${response.getErrors(0)}")
                Status.Error(Throwable(response.getErrors(0)))
            }
        } catch (error: Throwable) {
            Log.d("REPOSITORY", "USER REPOSITORY: ${error}")
            Status.Error(handleError(error))
        }
    }

    suspend fun login(email: String, password: String): Status<Unit> {
        return try {
            val loginRequest =
                LoginReq.newBuilder().setUsername(email).setPassword(password)
                    .build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.login(loginRequest)
            print(response)

            if (response.success) {
                saveAuthToken(response.jwt)
//                saveUserDetails(response.user)
                saveLoginInfo(email, password)

                Status.Success(Unit)
            } else {

                Status.Error(Throwable(response.getErrors(0)))
            }
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun loginInitiate(mobile: String): Status<Unit> {

        return try {
            val loginInitiateRq = ApiRpc.Username.newBuilder().setMobile(mobile).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.initiateLogin(loginInitiateRq)
            print("Response Now $response")

            if (response.success) {
                saveToken(response.initiateLoginJwt)
                Status.Success(Unit)
            } else {
                Status.Error(Throwable(response.getErrors(0)))
            }
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }

    }

    suspend fun loginValidate(otp: String,app_version: String, platform:String): Status<Unit> {

        return try {
            val loginValidateRq = ApiRpc.LoginValidateRequest.newBuilder().setOtp(otp).setAppVersion(app_version).setPlatform(platform)
                .setInitiateLoginJwt(PreferenceManager.getTokenInitation()).build()
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.loginValidate(loginValidateRq)

            if (response.success) {
                saveAuthToken(response.jwt)
                Status.Success(Unit)
            } else {
                Status.Error(Throwable(response.getErrors(0)))
            }
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }

    }


    private suspend fun saveUserDetails(user: User) {
//        database.userDao().saveUser(user)
    }

    private suspend fun saveLoginInfo(user: String, password: String) {
        withContext(Dispatchers.IO) {
            PreferenceManager.saveAccountLogin(user)
            PreferenceManager.saveAccountPassword(password)
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

//    fun getUserDetails(): LiveData<User> {
//        return database.userDao().getUser()
//    }

    companion object {

        const val OPEN_VPN_URL = "https://api2.dns.simplifyd.systems/v1/customer/vpn/profile"

    }
}