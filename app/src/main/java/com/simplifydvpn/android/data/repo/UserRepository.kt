package com.simplifydvpn.android.data.repo

import androidx.lifecycle.LiveData
import com.simplifydvpn.android.data.config.OpenVpnConfigurator
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.data.model.User
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class UserRepository : BaseRepository() {

    suspend fun login(email: String, password: String): Status<Unit> {
        return try {
            val loginResponse = apiService.login(hashMapOf(
                    "username" to email,
                    "password" to password
                ))

            saveAuthToken(loginResponse.jwt)
            saveUserDetails(loginResponse.user)
            saveLoginInfo(email, password)

            if (loginResponse.user.is_protected == null) {
                database.userDao().setProtectMe(false)
            }

            OpenVpnConfigurator.configureOVPNServers(OPEN_VPN_URL).first().let {
                PreferenceManager.saveProfileName(it.uuidString)
            }

            Status.Success(Unit)
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

    companion object{

        const val OPEN_VPN_URL = "https://api2.dns.simplifyd.systems/v1/customer/vpn/profile"

    }
}