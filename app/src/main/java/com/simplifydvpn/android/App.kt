package com.simplifydvpn.android

import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.onesignal.OneSignal
import com.simplifydvpn.android.data.config.OpenVpnConfigurator
import com.simplifydvpn.android.data.local.DatabaseManager
import com.simplifydvpn.android.data.local.PreferenceManager
import de.blinkt.openvpn.core.ICSOpenVPNApplication

class App : ICSOpenVPNApplication(), Configuration.Provider {

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        DatabaseManager.context = applicationContext
        PreferenceManager.context = applicationContext
        OpenVpnConfigurator.context = applicationContext

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init()
    }
}