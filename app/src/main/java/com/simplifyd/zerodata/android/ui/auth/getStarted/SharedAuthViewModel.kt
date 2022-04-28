package com.simplifyd.zerodata.android.ui.auth.getStarted

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.repo.UserRepository
import com.simplifyd.zerodata.android.utils.SingleLiveData
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedAuthViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val initiateStatus = SingleLiveData<Status<Unit>>()

    val validateStatus = MutableLiveData<Status<Unit>>()


    fun initiateLogin(phoneNumber: String) {
        initiateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginInitiate(phoneNumber)
                initiateStatus.postValue(result)
            }
        }

    }

    fun validateLogin(code: String, app_version: String, platform:String) {
        validateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginValidate(code, app_version, platform)
                validateStatus.postValue(result)
            }
        }
    }
}