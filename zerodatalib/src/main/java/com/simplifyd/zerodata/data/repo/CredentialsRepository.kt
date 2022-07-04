package com.simplifyd.zerodata.data.repo

import android.util.Log
import com.google.protobuf.ByteString
import com.simplifyd.zerodata.data.config.OpenVpnConfigurator
import com.simplifyd.zerodata.data.local.PreferenceManager
import com.simplifyd.zerodata.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.utils.Status
import com.simplifyd.zerodata.utils.handleError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import zerodata_api.ApiRpc
import zerodata_api.EdgeGrpc
import java.math.BigInteger
import java.security.interfaces.ECPublicKey
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class CredentialsRepository constructor(
    private val eCDHGenerator: ECDHGenerator = ECDHGenerator(),
    private val preferenceManager: PreferenceManager = PreferenceManager,
    private val openVpnConfigurator: OpenVpnConfigurator = OpenVpnConfigurator
) {

    suspend fun getConnectProfile(): Status<String> {
        return try {
            // generate our key pair
            val keypair = eCDHGenerator.generateKeyPair()
            val publicKey = keypair.public

            val affineX: BigInteger = (publicKey as ECPublicKey).w.affineX
            val affineY: BigInteger = publicKey.w.affineY

            val pubKey: ApiRpc.PubKey.Builder = ApiRpc.PubKey.newBuilder()
                .setX(ByteString.copyFrom(affineX.toByteArray()))
                .setY(ByteString.copyFrom(affineY.toByteArray()))

            Log.d("OVPN:", Arrays.toString(affineX.toByteArray()))
            Log.d("OVPN:", Arrays.toString(affineY.toByteArray()))

            val connectProfileRequest =
                ApiRpc.ConnectProfileReq.newBuilder().setClientPubKey(pubKey)
                    .build()

            val token = preferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel)
                .withCallCredentials(creds)
            val response = blockingStub.getConnectProfile(connectProfileRequest)

            Log.d("getConnectProfile: ", "Response $response")

            openVpnConfigurator.configureOVPNServers(profileData = response.unencryptedConnectProfile)
                .first().let {
                    preferenceManager.saveProfileName(it.uuidString)
                }
            Status.Success(response.openBrowserToUrl)

        } catch (error: Throwable) {
            error.printStackTrace()
            Status.Error(handleError(error))
        }
    }
}