package com.simplifyd.zerodata.android.ui.auth.getStarted

import androidx.lifecycle.LiveData
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

    val _initiateStatus = SingleLiveData<Status<Unit>>()
    val initiateStatus: LiveData<Status<Unit>> = _initiateStatus
    val _validateStatus = MutableLiveData<Status<Unit>>()
    val validateStatus:LiveData<Status<Unit>> = _validateStatus


    fun initiateLogin(phoneNumber: String) {
        _initiateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginInitiate(phoneNumber)
                _initiateStatus.postValue(result)
            }
        }

    }

    fun validateLogin(code: String, app_version: String, platform:String) {
        _validateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginValidate(code, app_version, platform)
                _validateStatus.postValue(result)
            }
        }
    }
}