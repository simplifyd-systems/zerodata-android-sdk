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
    private val dashboardRepository = DashboardRepository()
    private val settingsRepository = SettingsRepository()

    val getDashboardDataStatus = MutableLiveData<Status<DashboardData>>()

    fun logOut() {
        viewModelScope.launch {
            settingsRepository.logOut()
        }
    }

    val connectProfileStatus = MutableLiveData<Status<Unit>>()

    fun connect() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                /*
                    val channel = ManagedChannelBuilder
                        .forAddress("edge2.simplifyd.net", 30000)
                        .usePlaintext()
                        .build()

                    val loginRequest =
                        ApiRpc.LoginReq.newBuilder().setUsername("tomi@amao.io").setPassword("password")
                            .build();
                    val blockingStub = EdgeGrpc.newBlockingStub(channel)
                    val response = blockingStub.login(loginRequest)
                    print(response)
*/
                val channel = ManagedChannelBuilder
                    .forAddress("edge2.simplifyd.net", 30000)
                    .usePlaintext()
                    .build()

                val eCDHGenerator = ECDHGenerator()
                val keypair = eCDHGenerator.generateKeyPair()

                val publicKey = keypair.public

                val affineX: BigInteger = (publicKey as ECPublicKey).w.affineX
                val affineY: BigInteger = (publicKey as ECPublicKey).w.affineY

                val pubKey: ApiRpc.PubKey.Builder = ApiRpc.PubKey.newBuilder()
                    .setX(ByteString.copyFrom(affineX.toByteArray()))
                    .setY(ByteString.copyFrom(affineY.toByteArray()))

                val connectProfileRequest =
                    ApiRpc.ConnectProfileReq.newBuilder().setClientPubKey(pubKey)
                        .build();

                val token = PreferenceManager.getToken()
                print(token)
                val creds = AuthenticationCallCredentials(token)
                val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
                    .withCallCredentials(creds)
                val response = blockingStub.getConnectProfile(connectProfileRequest)
                print(response)

                //load KEY2 from server public key
                val ecParameters = publicKey.params
                val lengthX = response.serverPubKey.x.toByteArray().size
                val lengthY = response.serverPubKey.y.toByteArray().size
                val x = BigInteger(response.serverPubKey.x.toByteArray());
                val y = BigInteger(response.serverPubKey.y.toByteArray());
                val ecPoint = ECPoint(x, y);
                val keySpec = ECPublicKeySpec(ecPoint, ecParameters);
                val keyFactory = KeyFactory.getInstance("EC")
                val serverPublicKey: PublicKey
                try {
                    serverPublicKey = keyFactory.generatePublic(keySpec)


                // Perform key agreement
                // Perform key agreement
                /*
                val ka: KeyAgreement = KeyAgreement.getInstance("ECDH")
                ka.init(keypair.private)
                ka.doPhase(serverPublicKey, true)

                // Read shared secret
                val sharedSecret: ByteArray = ka.generateSecret()
                val sharedSecretStr = String(sharedSecret)
                 */

                // Create a shared secret based on our private key and the other party's public key.
                // Create a shared secret based on our private key and the other party's public key.
                val keyAgreement = KeyAgreement.getInstance("ECDH", "AndroidKeyStore")
                keyAgreement.init(keypair.private)
                keyAgreement.doPhase(serverPublicKey, true)
                val sharedSecret = keyAgreement.generateSecret()
                val sharedSecretStr = String(sharedSecret)
                // val result = userRepository.connectProfile()
                // connectProfileStatus.postValue(result)
            }
                catch(e: InvalidKeySpecException) {
                    println(e)
                }

                Log.d("***************************", "HEREEEEEEEEEEE")
            }
        }
    }
}