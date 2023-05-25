package com.simplifyd.zerodata

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.simplifyd.zerodata.data.config.OpenVpnConfigurator
import com.simplifyd.zerodata.data.local.PreferenceManager
import com.simplifyd.zerodata.data.repo.CredentialsRepository
import com.simplifyd.zerodata.data.repo.InitializationRepository
import com.simplifyd.zerodata.settings.ZeroDataSDKSettings
import com.simplifyd.zerodata.settings.ZeroDataStateListener
import com.simplifyd.zerodata.utils.Status
import de.blinkt.openvpn.LaunchVPN
import de.blinkt.openvpn.VpnProfile
import de.blinkt.openvpn.activities.DisconnectVPN
import de.blinkt.openvpn.core.ConnectionStatus
import de.blinkt.openvpn.core.ProfileManager
import de.blinkt.openvpn.core.VpnStatus
import kotlinx.coroutines.*
/**Created by Moronke Anifowose
 * Establish a VPN connection to access the internet datafree**/

@ExperimentalCoroutinesApi
class ZeroDataSDK(private val context: Application, private val listener: ZeroDataStateListener) :
    VpnStatus.StateListener {

    private val credentialsRepository = CredentialsRepository()
    private val initializationRepository = InitializationRepository()
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    init {
        PreferenceManager.context = context
        OpenVpnConfigurator.context = context
    }

    //Initialize SDK, prerequisite for establishing Zerodata connection
    fun configure(settings: ZeroDataSDKSettings) {
        scope.launch {
            when (val response = initialize(settings.userID)) {
                is Status.Success -> listener.initializationSuccess()

                is Status.Error -> listener.initializationFailed(response.error.message.toString())

            }
        }
    }

    //Establish connection with Zerodata
    fun connectToZerodata(activity: Activity) {
        scope.launch {
            when (val response = connectZeroData()) {
                is Status.Success -> initiateToggleOpenVPNConnection(activity)
                is Status.Error -> listener.configureFailed(response.error.message.toString())

            }
        }
    }

    //Disconnects established connection with Zerodata
    fun disconnectFromZerodata(activity: Activity) {
        disconnectVPNConnection(activity)
    }

    //Checks if there is an established connection
    fun status(): Boolean {
        return isVPNConnected()
    }

    private suspend fun initialize(userId: String): Status<Unit> {
        return initializationRepository.initialize(context.packageName, userId)
    }


    private fun initiateToggleOpenVPNConnection(activity: Activity) {
        PreferenceManager.getProfileName()?.let {
            toggleOpenVPNConnection(
                ProfileManager.get(
                    activity,
                    it
                ), activity
            )
        }
    }

    private suspend fun connectZeroData(): Status<String> {
        return credentialsRepository.getConnectProfile()
    }

    private fun toggleOpenVPNConnection(profile: VpnProfile, activity: Activity) {
        if (isVPNConnected() && profile.uuidString == VpnStatus.getLastConnectedVPNProfile()) {
            disconnectVPNConnection(activity)
        } else {
            launchOpenVPNConnection(profile, activity)
        }
    }

    private fun launchOpenVPNConnection(profile: VpnProfile, activity: Activity) {
        val intent = Intent(activity, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.action = Intent.ACTION_MAIN
        activity.startActivity(intent)
    }

    private fun disconnectVPNConnection(activity: Activity) {
        val disconnectVPN = Intent(activity, DisconnectVPN::class.java)
        activity.startActivity(disconnectVPN)
    }

    private fun isVPNConnected() = VpnStatus.isVPNActive()

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        listener.updateState(state, logmessage, localizedResId, level, Intent)
    }

    override fun setConnectedVPN(uuid: String?) {
        listener.setConnectedVPN(uuid)
    }
}
