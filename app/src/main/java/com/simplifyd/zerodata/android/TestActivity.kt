package com.simplifyd.zerodata.android

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.ZeroDataSDK
import com.simplifyd.zerodata.settings.ZeroDataSDKSettings
import com.simplifyd.zerodata.settings.ZeroDataStateListener
import de.blinkt.openvpn.core.ConnectionStatus
import kotlinx.android.synthetic.main.activity_test.*
import java.util.logging.Level

class TestActivity : AppCompatActivity(), ZeroDataStateListener {
    var isTurnedOn = false

    private val checkChangedListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.not()) {
                connect_switch.isChecked = false
                ZeroDataSDK(this).disconnectZeroData()

            } else {
                ZeroDataSDK(this).connectZeroData()
                connect_switch.isChecked = true
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        ZeroDataSDK(this).configure(ZeroDataSDKSettings())

        connect_switch.setOnCheckedChangeListener(checkChangedListener)


        btnSubmit.setOnClickListener {
            if (isTurnedOn) {
                ZeroDataSDK(this).disconnectZeroData()
                btnSubmit.text = "Turn on"
                isTurnedOn = false


            } else {
                ZeroDataSDK(this).connectZeroData()

                btnSubmit.text = "Connecting.."
                isTurnedOn = true


            }

        }


    }

    override fun updateState(
        state: String?,
        logmessage: String?,
        localizedResId: Int,
        level: ConnectionStatus?,
        Intent: Intent?
    ) {
        if (level == ConnectionStatus.LEVEL_NOTCONNECTED){

        }
    }

    override fun setConnectedVPN(uuid: String?) {
        TODO("Not yet implemented")
    }


}