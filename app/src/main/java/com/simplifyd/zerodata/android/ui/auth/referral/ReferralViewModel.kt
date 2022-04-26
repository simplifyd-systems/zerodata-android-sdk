package com.simplifyd.zerodata.android.ui.auth.referral

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.repo.UserRepository
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReferralViewModel:ViewModel(){

    private val userRepository = UserRepository()

    val logReferralCode = MutableLiveData<Status<Unit>>()

    fun validateReferral(code: String) {
        logReferralCode.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                val result = userRepository.loginValidate(code, app_version, platform)
//                logReferralCode.postValue(result)
            }
        }
    }

}