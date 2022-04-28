package com.simplifyd.zerodata.android.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.ui.main.MainActivity


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!PreferenceManager.getToken().isNullOrEmpty()) {
            goToMainScreen()
        }

    }


    private fun goToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}