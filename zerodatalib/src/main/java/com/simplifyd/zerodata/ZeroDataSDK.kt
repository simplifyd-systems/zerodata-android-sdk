package com.simplifyd.zerodata

import android.app.Activity
import android.content.Intent
import com.simplifyd.zerodata.data.config.OpenVpnConfigurator
import com.simplifyd.zerodata.data.local.PreferenceManager
import com.simplifyd.zerodata.data.repo.CredentialsRepository
import com.simplifyd.zerodata.data.repo.UserRepository
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


@ExperimentalCoroutinesApi
class ZeroDataSDK(private val activity: Activity) : VpnStatus.StateListener {

    private var listener: ZeroDataStateListener? = null
    private lateinit var settings: ZeroDataSDKSettings
    private val userRepository = UserRepository()
    private val credentialsRepository = CredentialsRepository()
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    init {
        PreferenceManager.context = activity
        OpenVpnConfigurator.context = activity
    }

    fun configure(settings: ZeroDataSDKSettings) {
        this.settings = settings

        scope.launch {
            val response = initate()
            if (response is Status.Success) {
                validate()
            }
        }
    }

    fun addListener(listener: ZeroDataStateListener) {
        this.listener = listener
        VpnStatus.addStateListener(this)
    }

    fun removeListener() {
        this.listener = null
        VpnStatus.removeStateListener(this)
    }

    private suspend fun initate(): Status<Unit> {
        return userRepository.loginInitiate("2347234567890")
    }

    private suspend fun validate() {
        userRepository.loginValidate(
            "999999", BuildConfig.VERSION_CODE.toString(),
            "android"
        )
    }

    fun connectZeroData() {
        scope.launch {
            val response = connectToZeroData()
            if (response is Status.Success) {
                PreferenceManager.getProfileName()?.let {
                    startOrStopOpenVPN(
                        ProfileManager.get(
                            activity,
                            it
                        )
                    )
                }
            }
        }
    }

    private suspend fun connectToZeroData(): Status<String> {
        return credentialsRepository.getConnectProfile()
    }

    private fun startOrStopOpenVPN(profile: VpnProfile) {
        if (isVPNConnected() && profile.uuidString == VpnStatus.getLastConnectedVPNProfile()) {
            disconnectVPN()
        } else {
            startOpenVPN(profile)
        }
    }

    private fun disconnectVPN() {
        val disconnectVPN = Intent(activity, DisconnectVPN::class.java)
        activity.startActivity(disconnectVPN)
    }

    private fun isVPNConnected() =
        VpnStatus.isVPNActive()


    private fun startOpenVPN(profile: VpnProfile) {
        val intent = Intent(activity, LaunchVPN::class.java)
        intent.putExtra(LaunchVPN.EXTRA_KEY, profile.uuid.toString())
        intent.action = Intent.ACTION_MAIN
        activity.startActivity(intent)
    }

    fun disconnectZeroData() {
        disconnectVPN()
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        listener?.updateState(state, logmessage, localizedResId, level, Intent)
    }

    override fun setConnectedVPN(uuid: String?) {
        listener?.setConnectedVPN(uuid)
    }
}
