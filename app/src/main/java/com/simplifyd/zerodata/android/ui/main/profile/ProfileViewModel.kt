package com.simplifyd.zerodata.android.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.model.User
import com.simplifyd.zerodata.android.data.repo.SettingsRepository
import com.simplifyd.zerodata.android.data.repo.UserRepository
import com.simplifyd.zerodata.android.utils.SingleLiveData
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val settingsRepository = SettingsRepository()

    val editProfileStatus = SingleLiveData<Status<Unit>>()
    val setNotifyMeStatus = SingleLiveData<Status<Unit>>()
    val getNotifyMeStatus = SingleLiveData<Status<Boolean>>()

//    val getUserDetailsStatus: LiveData<User>
//        get() = userRepository.getUserDetails()

    var user: User? = null

    init {
        getNotifyMe()
    }

    private fun getNotifyMe() {
        getNotifyMeStatus.postValue(Status.Loading)

        try {
            viewModelScope.launch {
                val notifyMe = settingsRepository.getNotifyMe()
                getNotifyMeStatus.postValue(Status.Success(notifyMe))
            }
        } catch (error: Throwable) {
            getNotifyMeStatus.postValue(Status.Error(error))
        }
    }

    fun updateUserDetails(fullName: String, phoneNumber: String) {
        editProfileStatus.postValue(Status.Loading)

        try {
            viewModelScope.launch {
                settingsRepository.updateUserDetails(fullName, phoneNumber)
                editProfileStatus.postValue(Status.Success(Unit))
            }
        } catch (error: Throwable) {
            editProfileStatus.postValue(Status.Error(error))
        }
    }

    fun setNotifyMe(notifyMe: Boolean) {
        setNotifyMeStatus.postValue(Status.Loading)

        try {
            viewModelScope.launch {
                settingsRepository.setNotifyMe(notifyMe)
                setNotifyMeStatus.postValue(Status.Success(Unit))
            }
        } catch (error: Throwable) {
            setNotifyMeStatus.postValue(Status.Error(error))
        }
    }

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }
}