package com.simplifydvpn.android.ui.main.overview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.ByteString
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.data.model.DashboardData
import com.simplifydvpn.android.data.remote.grpc.GRPCChannelFactory
import com.simplifydvpn.android.data.repo.DashboardRepository
import com.simplifydvpn.android.data.repo.ECDHGenerator
import com.simplifydvpn.android.data.repo.SettingsRepository
import com.simplifydvpn.android.data.repo.UserRepository
import com.simplifydvpn.android.data.repo.AuthenticationCallCredentials
import com.simplifydvpn.android.data.repo.CredentialsRepository
import com.simplifydvpn.android.utils.Status
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Hex
import pb.ApiRpc
import pb.EdgeGrpc
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Security
import java.security.interfaces.ECPublicKey
import java.security.spec.*
import javax.crypto.KeyAgreement

class OverviewViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val credentialsRepository = CredentialsRepository()
    private val settingsRepository = SettingsRepository()

    val getDashboardDataStatus = MutableLiveData<Status<DashboardData>>()
    
    init{
        connect()
    }

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }

    val connectProfileStatus = MutableLiveData<Status<Unit>>()

    fun connect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = credentialsRepository.getConnectProfile()
                print(result)
                connectProfileStatus.postValue(result)
            }
        }
    }
}