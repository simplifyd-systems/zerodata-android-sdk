package com.simplifyd.zerodata.android.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.auth.LoginActivity


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimary)
        }
        setContentView(R.layout.activity_splash_screen)
        checkSession()
    }


    private fun checkSession() {
        Handler().postDelayed({
            startAuthActivity()
        }, 1000)
    }

    private fun startAuthActivity() {
        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        finish()
    }
}