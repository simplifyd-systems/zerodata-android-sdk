package com.simplifyd.zerodata.android

import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import com.onesignal.OneSignal
import com.simplifyd.zerodata.android.data.config.OpenVpnConfigurator
import com.simplifyd.zerodata.android.data.local.DatabaseManager
import com.simplifyd.zerodata.android.data.local.PreferenceManager
import de.blinkt.openvpn.core.ICSOpenVPNApplication

class App : ICSOpenVPNApplication(), Configuration.Provider {

    private val ONESIGNAL_APP_ID = "ccf04af8-42e5-40cf-ac4c-e8be7e2b89a9"


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

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

//        OneSignal.startInit(this)
//            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init()
    }
}