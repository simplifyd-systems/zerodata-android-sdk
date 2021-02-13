package com.simplifydvpn.android.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.simplifydvpn.android.data.config.OpenVpnConfigurator
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.data.model.User
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserRepository : BaseRepository() {

    suspend fun login(email: String, password: String): Status<Unit> {
        return try {
            val loginResponse = apiService.login(
                hashMapOf(
                    "username" to email,
                    "password" to password
                )
            )

            saveAuthToken(loginResponse.jwt)
            saveUserDetails(loginResponse.user)

            if (loginResponse.user.is_protected == null) {
                database.userDao().setProtectMe(false)
            }

            OpenVpnConfigurator.configureOVPNServers("https://docs.google.com/uc?export=download&id=1jTUKvt0RwpAftwaXaomxj-lXCaypWUr4").first().let {
                Log.d("UUID for protocol UDP." , "${it.uuidString} ${it.name}")
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

    private suspend fun saveAuthToken(token: String) {
        withContext(Dispatchers.IO) {
            PreferenceManager.saveToken(token)
        }
    }

    fun getUserDetails(): LiveData<User> {
        return database.userDao().getUser()
    }
}