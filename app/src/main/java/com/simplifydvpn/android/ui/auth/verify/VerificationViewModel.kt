package com.simplifydvpn.android.ui.auth.verify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerificationViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val validateStatus = MutableLiveData<Status<Unit>>()

    fun validateLogin(code: String) {
        validateStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.loginValidate(code)
                validateStatus.postValue(result)
            }
        }
    }


}