package com.simplifydvpn.android.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.local.PreferenceManager
import com.simplifydvpn.android.ui.main.MainActivity

class LoginActivity : AppCompatActivity(R.layout.activity_login) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PreferenceManager.getToken().isNullOrEmpty()) {
            goToMainScreen()
        }

    }



    private fun goToForgotPasswordScreen() {

    }


    private fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}