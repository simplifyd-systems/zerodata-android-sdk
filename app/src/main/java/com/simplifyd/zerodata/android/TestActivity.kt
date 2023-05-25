package com.simplifyd.zerodata.android

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.ZeroDataSDK
import com.simplifyd.zerodata.settings.ZeroDataSDKSettings
import com.simplifyd.zerodata.settings.ZeroDataStateListener
import de.blinkt.openvpn.core.ConnectionStatus
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
class TestActivity : AppCompatActivity(), ZeroDataStateListener {

    private val zerodataSDK: ZeroDataSDK by lazy { ZeroDataSDK(application, this) }

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    private val checkChangedListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked.not()) {
                connect_switch.isChecked = false
                zerodataSDK.disconnectFromZerodata(this)

            } else {
                zerodataSDK.connectToZerodata(this)
                connect_switch.isChecked = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        zerodataSDK.configure(ZeroDataSDKSettings("2347234567890"))
        connect_switch.setOnCheckedChangeListener(checkChangedListener)
    }

    private fun handleError(error: String) {
        scope.launch {
            enableSwitch(false)
            Toast.makeText(this@TestActivity, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableSwitch(enable: Boolean) {
        scope.launch {
            connect_switch.isEnabled = enable
        }
    }

    override fun initializationSuccess() {
        enableSwitch(true)
    }

    override fun initializationFailed(error: String) {
        handleError(error)
    }

    override fun configureSuccess() {
        enableSwitch(true)
    }

    override fun configureFailed(error: String) {
        handleError(error)
    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {}

    override fun setConnectedVPN(uuid: String?) {}
}