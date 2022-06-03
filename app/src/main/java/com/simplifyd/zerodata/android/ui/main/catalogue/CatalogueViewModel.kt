package com.simplifyd.zerodata.android.ui.main.catalogue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simplifyd.zerodata.android.data.model.NotificationData
import com.simplifyd.zerodata.android.data.repo.CatalogueRepository
import com.simplifyd.zerodata.android.ui.main.catalogue.mapper.ListedAppMapper
import com.simplifyd.zerodata.android.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zerodata_api.ApiRpc

class CatalogueViewModel: ViewModel() {

    private val catalogueRepository = CatalogueRepository()
    private val listedAppMapper = ListedAppMapper()

    private val _loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    val _fetchListedApp = MutableLiveData<List<ListedApp>>()
    val fetchListedApp: LiveData<List<ListedApp>> = _fetchListedApp

    fun fetchListedApps(){
        _loading.postValue(true)

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result: Status<List<ApiRpc.ListedApps.ListedApp>> = catalogueRepository.getListedApps()


                when(result){

                    is Status.Loading ->{
                        _loading.postValue(true)
                    }

                    is Status.Success ->{
                        _loading.postValue(false)
                        val data = result.data
                        _fetchListedApp.postValue(listedAppMapper.mapToUIList(data))
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