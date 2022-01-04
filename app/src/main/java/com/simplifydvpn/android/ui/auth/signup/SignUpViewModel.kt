package com.simplifydvpn.android.ui.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.utils.Status
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val loginStatus = MutableLiveData<Status<Unit>>()

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ) {
        loginStatus.postValue(Status.Loading)

        viewModelScope.launch {
            val result = userRepository.signUp(firstName, lastName, email, phoneNumber, password)
            loginStatus.postValue(result)
        }
    }
}