package com.simplifydvpn.android.data.repo

import androidx.lifecycle.LiveData
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError

class MainRepository : BaseRepository() {

    suspend fun setProtectMe(protectMe: Boolean): Status<Unit> {
        return try {
            database.userDao().setProtectMe(protectMe)

            if (protectMe) {
                apiService.protectUser()
            } else {
                apiService.unProtectUser()
            }

            Status.Success(Unit)
        } catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }

    fun getProtectMe(): LiveData<Boolean> {
        return database.userDao().getProtectMe()
    }
}