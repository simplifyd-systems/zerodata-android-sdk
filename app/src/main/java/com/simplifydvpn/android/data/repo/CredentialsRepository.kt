package com.simplifydvpn.android.data.repo

import android.util.Log
import pb.ApiRpc.ConnectProfileReq
import java.math.BigInteger
import java.security.interfaces.ECPublicKey
import com.simplifydvpn.android.data.remote.grpc.GRPCChannelFactory
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import io.grpc.ManagedChannelBuilder
import pb.ApiRpc
import pb.ApiRpc.LoginReq
import pb.EdgeGrpc
import pb.ApiRpc.RegisterReq
import androidx.lifecycle.LiveData
import com.google.protobuf.ByteString
import com.simplifydvpn.android.data.config.OpenVpnConfigurator
import com.simplifydvpn.android.data.local.PreferenceManager
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec
import java.security.spec.InvalidKeySpecException
import javax.crypto.KeyAgreement
import java.util.Arrays

class CredentialsRepository constructor(private val eCDHGenerator: ECDHGenerator = ECDHGenerator(),
                                        private val preferenceManager: PreferenceManager = PreferenceManager,
                                        private val openVpnConfigurator: OpenVpnConfigurator = OpenVpnConfigurator) {

    suspend fun getConnectProfile(): Status<String> {
        return try {
            // generate our key pair
            val keypair = eCDHGenerator.generateKeyPair()
            val publicKey = keypair.public

            val affineX: BigInteger = (publicKey as ECPublicKey).w.affineX
            val affineY: BigInteger = (publicKey as ECPublicKey).w.affineY

            val pubKey: ApiRpc.PubKey.Builder = ApiRpc.PubKey.newBuilder()
                .setX(ByteString.copyFrom(affineX.toByteArray()))
                .setY(ByteString.copyFrom(affineY.toByteArray()))

            Log.d("OVPN:", "${Arrays.toString(affineX.toByteArray())}")
            Log.d("OVPN:", "${Arrays.toString(affineY.toByteArray())}")

            val connectProfileRequest =
                ApiRpc.ConnectProfileReq.newBuilder().setClientPubKey(pubKey)
                    .build();

            val token = preferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
                .withCallCredentials(creds)
            val response = blockingStub.getConnectProfile(connectProfileRequest)

            openVpnConfigurator.configureOVPNServers(profileData = response.unencryptedConnectProfile).first().let {
                preferenceManager.saveProfileName(it.uuidString)
            }

            print(response)
            Status.Success(response.openBrowserToUrl)
        } catch (error: Throwable) {
            error.printStackTrace()
            Status.Error(handleError(error))
        }
    }


    /*fun loadKeysDecryptionKeys() {
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
            *//*
            val ka: KeyAgreement = KeyAgreement.getInstance("ECDH")
            ka.init(keypair.private)
            ka.doPhase(serverPublicKey, true)

            // Read shared secret
            val sharedSecret: ByteArray = ka.generateSecret()
            val sharedSecretStr = String(sharedSecret)
             *//*

            // Create a shared secret based on our private key and the other party's public key.
            // Create a shared secret based on our private key and the other party's public key.
            val keyAgreement = KeyAgreement.getInstance("ECDH", "AndroidKeyStore")
            keyAgreement.init(keypair.private)
            keyAgreement.doPhase(serverPublicKey, true)
            val sharedSecret = keyAgreement.generateSecret()
            val sharedSecretStr = String(sharedSecret)
            // val result = userRepository.connectProfile()
            // connectProfileStatus.postValue(result)
        } catch (e: InvalidKeySpecException) {
            println(e)
        }

        Log.d("***************************", "HEREEEEEEEEEEE")
    }*/
}