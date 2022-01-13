package com.simplifydvpn.android.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val loginStatus = MutableLiveData<Status<Unit>>()

    fun login(email: String, password: String) {
        loginStatus.postValue(Status.Loading)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = userRepository.login(email, password)
                loginStatus.postValue(result)
            }
        }
    }
}