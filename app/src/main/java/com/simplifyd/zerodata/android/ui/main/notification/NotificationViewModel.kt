package com.simplifyd.zerodata.android.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.model.NotificationData
import com.simplifyd.zerodata.android.data.repo.NotificationRepository
import com.simplifyd.zerodata.android.ui.main.notification.mapper.NotificationMapper
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pb.ApiRpc

class NotificationViewModel : ViewModel() {

    private val notificationRepository = NotificationRepository()

    private val notificationMapper = NotificationMapper()

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    val _fetchNotification = MutableLiveData<List<NotificationData>>()
    val fetchNotification: LiveData<List<NotificationData>> = _fetchNotification

    fun fetchNotifications(){

        _loading.postValue(true)

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result: Status<List<ApiRpc.Notification>> = notificationRepository.getNotifications()


            when(result){

                is Status.Loading ->{
                    _loading.postValue(true)
                }

                is Status.Success ->{
                    _loading.postValue(false)
                    val data = result.data
                    _fetchNotification.postValue(notificationMapper.mapToUIList(data))
                }



                is Status.Error ->{
                    _loading.postValue(false)
                    _message.postValue(result.error.localizedMessage)
                }


            }
        }
        }
    }




}