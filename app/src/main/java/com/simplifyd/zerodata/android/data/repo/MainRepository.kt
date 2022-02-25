package com.simplifyd.zerodata.android.data.repo

import androidx.lifecycle.LiveData
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError

class MainRepository : BaseRepository() {

///    suspend fun setProtectMe(protectMe: Boolean): Status<Unit> {
//        return try {
//            database.userDao().setProtectMe(protectMe)
//
//            if (protectMe) {
//                apiService.protectUser()
//            } else {
//                apiService.unProtectUser()
//            }
//
//            Status.Success(Unit)
//        } catch (error: Throwable) {
//            Status.Error(handleError(error))
//        }
//    }

//    fun getProtectMe(): LiveData<Boolean> {
//        return database.userDao().getProtectMe()
//    }
}