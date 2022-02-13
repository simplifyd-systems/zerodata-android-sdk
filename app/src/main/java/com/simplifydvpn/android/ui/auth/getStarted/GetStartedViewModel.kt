package com.simplifydvpn.android.ui.auth.getStarted

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GetStartedViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val initiateStatus = MutableLiveData<Status<Unit>>()

    fun initiateLogin(phoneNumber: String) {
        initiateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginInitiate(phoneNumber)
                initiateStatus.postValue(result)
            }
        }
    }


}