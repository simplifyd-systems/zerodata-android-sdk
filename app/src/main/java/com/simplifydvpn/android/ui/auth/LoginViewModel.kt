package com.simplifydvpn.android.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val userRepository = UserRepository()

    val loginStatus = MutableLiveData<Status<Unit>>()

    fun login(email: String, password: String) {
        loginStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = userRepository.login(email, password)
            loginStatus.postValue(result)
        }
    }
}