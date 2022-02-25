package com.simplifyd.zerodata.android.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.ui.main.MainActivity
import io.grpc.ManagedChannelBuilder
import pb.ApiRpc.LoginReq
import pb.EdgeGrpc


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PreferenceManager.getToken().isNullOrEmpty()) {
            goToMainScreen()
        }

        // connectToChannel()
    }

    fun connectToChannel() {
        val channel = ManagedChannelBuilder
            .forAddress("edge2.simplifyd.net", 30000)
            .usePlaintext()
            .build()

        val loginRequest =
            LoginReq.newBuilder().setUsername("tomi@amao.io").setPassword("password")
                .build();
        val blockingStub = EdgeGrpc.newBlockingStub(channel)
        val response = blockingStub.login(loginRequest)
        print(response)
        channel.shutdown();
        PreferenceManager.saveToken(response.jwt)
    }

    private fun goToForgotPasswordScreen() {

    }


    private fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}