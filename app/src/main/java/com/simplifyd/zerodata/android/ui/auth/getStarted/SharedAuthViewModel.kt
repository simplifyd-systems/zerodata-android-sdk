package com.simplifyd.zerodata.android.ui.auth.getStarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.Api
import com.simplifyd.zerodata.android.data.model.InstalledAppData
import com.simplifyd.zerodata.android.data.repo.DataGatheringRepository
import com.simplifyd.zerodata.android.data.repo.UserRepository
import com.simplifyd.zerodata.android.ui.auth.verify.InstalledAppMapper
import com.simplifyd.zerodata.android.ui.main.notification.mapper.NotificationMapper
import com.simplifyd.zerodata.android.utils.SingleLiveData
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zerodata_api.ApiRpc

class SharedAuthViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val installedAppMapper = InstalledAppMapper()
    private val dataGatheringRepository = DataGatheringRepository()

    val _initiateStatus = SingleLiveData<Status<Unit>>()
    val initiateStatus: LiveData<Status<Unit>> = _initiateStatus
    val _validateStatus = MutableLiveData<Status<Unit>>()
    val validateStatus:LiveData<Status<Unit>> = _validateStatus
    val _postDeviceInfo = MutableLiveData<Status<Unit>>()
    val postDeviceInfo:LiveData<Status<Unit>> = _postDeviceInfo

    val _postInstalledApps = MutableLiveData<Status<Unit>>()
    val postInstalledApps:LiveData<Status<Unit>> = _postInstalledApps


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

    fun postDeviceInfo(manufacturer: String, brand: String, model: String) {
        _postDeviceInfo.postValue(Status.Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = dataGatheringRepository.postDeviceModel(manufacturer, brand, model)
                _postDeviceInfo.postValue(result)
            }
        }
    }

    fun postInstalledApps(installedApps: List<InstalledAppData>){
        _postInstalledApps.postValue(Status.Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = dataGatheringRepository.postInstalledApps(installedAppMapper.mapFromUIList(installedApps))
                _postInstalledApps.postValue(result)
            }
        }





    }
}