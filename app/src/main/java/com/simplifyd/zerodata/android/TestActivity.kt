package com.simplifyd.zerodata.android

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.ZeroDataSDK
import com.simplifyd.zerodata.settings.ZeroDataSDKSettings
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    var isTurnedOn = false

    private val checkChangedListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.not()) {
                connect_switch.isChecked = false
                ZeroDataSDK(this).disconnectFromZeroData()

            } else {
                ZeroDataSDK(this).turnOnZeroData()
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
                ZeroDataSDK(this).disconnectFromZeroData()
                btnSubmit.text = "Turn on"
                isTurnedOn = false


            } else {
                ZeroDataSDK(this).turnOnZeroData()

                btnSubmit.text = "Connecting.."
                isTurnedOn = true


            }

        }


    }


}