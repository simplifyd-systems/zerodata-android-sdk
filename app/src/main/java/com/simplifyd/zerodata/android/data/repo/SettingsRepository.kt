package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepository : BaseRepository() {

    suspend fun updateUserDetails(fullName: String, phoneNumber: String) {

    }

    suspend fun setNotifyMe(notifyMe: Boolean) {
        withContext(Dispatchers.IO) {
            PreferenceManager.setNotifyMe(notifyMe)
        }
    }

    suspend fun getNotifyMe(): Boolean {
        return withContext(Dispatchers.IO) {
            PreferenceManager.getNotifyMe()
        }
    }

    suspend fun logOut() {
        withContext(Dispatchers.IO) {
            PreferenceManager.clearAll()
            database.clearAllTables()
        }
    }
}