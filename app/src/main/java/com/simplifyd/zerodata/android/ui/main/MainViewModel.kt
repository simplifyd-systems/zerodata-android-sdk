package com.simplifyd.zerodata.android.ui.main

import androidx.lifecycle.ViewModel
import com.simplifyd.zerodata.android.data.repo.MainRepository
import com.simplifyd.zerodata.android.utils.SingleLiveData
import com.simplifyd.zerodata.android.utils.Status

class MainViewModel : ViewModel() {

    private val mainRepository = MainRepository()

    val setProtectMeStatus = SingleLiveData<Status<Unit>>()

//    val getProtectMeStatus: LiveData<Boolean>
//        get() = mainRepository.getProtectMe()

//    fun setProtectMe(protectMe: Boolean) {
//        setProtectMeStatus.postValue(Status.Loading)
//
//        viewModelScope.launch {
//            val result = mainRepository.setProtectMe(protectMe)
//            setProtectMeStatus.postValue(result)
//        }
//    }
}