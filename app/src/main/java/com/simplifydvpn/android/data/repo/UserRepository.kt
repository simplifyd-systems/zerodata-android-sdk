package com.simplifydvpn.android.data.repo

import androidx.lifecycle.LiveData
import com.simplifydvpn.android.data.config.OpenVpnConfigurator
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.data.model.User
import com.simplifydvpn.android.data.remote.grpc.GRPCChannelFactory
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import io.grpc.ManagedChannelBuilder
import pb.ApiRpc
import pb.ApiRpc.LoginReq
import pb.EdgeGrpc
import pb.ApiRpc.RegisterReq

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
            print(response)
            Status.Success(Unit)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    suspend fun login(email: String, password: String): Status<Unit> {
        return try {
            val loginRequest =
                LoginReq.newBuilder().setUsername(email).setPassword(password)
                    .build();
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
            val response = blockingStub.login(loginRequest)
            print(response)

            if (response.success) {
                saveAuthToken(response.jwt)
                //saveUserDetails(response.user)
                saveLoginInfo(email, password)

                OpenVpnConfigurator.configureOVPNServers(OPEN_VPN_URL).first().let {
                    PreferenceManager.saveProfileName(it.uuidString)
                }

                val eCDHGenerator = ECDHGenerator()
                eCDHGenerator.generateKey()

                Status.Success(Unit)
            } else {
                Status.Error(Throwable(response.getErrors(0) ))
            }
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    private suspend fun saveUserDetails(user: User) {
        database.userDao().saveUser(user)
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

    fun getUserDetails(): LiveData<User> {
        return database.userDao().getUser()
    }

    companion object {

        const val OPEN_VPN_URL = "https://api2.dns.simplifyd.systems/v1/customer/vpn/profile"

    }
}