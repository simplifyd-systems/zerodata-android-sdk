package com.simplifydvpn.android.data.local

import android.content.Context

object PreferenceManager {

    private const val BEARER_TOKEN = "BEARER_TOKEN"
    private const val TOKEN = "TOKEN"
    private const val NOTIFY_ME = "NOTIFY_ME"
    private const val IS_SEEN = "IS_SEEN"
    private const val APP_NAME = "APP_NAME"
    private const val VPN_PROFILE_NAME = "VPN_PROFILE_NAME"

    private const val VPN_USERNAME_NAME = "VPN_PROFILE_NAME"
    private const val VPN_PASSWORD = "VPN_PROFILE_PASSWORD"

    lateinit var context: Context

    private val preferences by lazy {
        context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
    }

    fun getAccountPassword():String? = preferences.getString(VPN_PASSWORD, null)

    fun getAccountLogin():String? = preferences.getString(VPN_USERNAME_NAME, null)

    fun saveAccountPassword(password: String) {
        preferences.edit().apply {
            putString(VPN_PASSWORD, password)
        }.apply()
    }

    fun saveAccountLogin(login: String) {
        preferences.edit().apply {
            putString(VPN_USERNAME_NAME, login)
        }.apply()
    }

    fun saveProfileName(name: String) {
        preferences.edit().apply {
            putString(VPN_PROFILE_NAME, name)
        }.apply()
    }

    fun getProfileName():String? = preferences.getString(VPN_PROFILE_NAME, null)

    fun saveToken(token: String) {
        preferences.edit().apply {
            putString(BEARER_TOKEN, token)
        }.apply()
    }

    fun getToken(): String? = preferences.getString(BEARER_TOKEN, null)

    fun saveTokenInitation(token: String) {
        preferences.edit().apply {
            putString(TOKEN, token)
        }.apply()
    }

    fun getTokenInitation(): String? = preferences.getString(TOKEN, null)

    fun setNotifyMe(notifyMe: Boolean) {
        preferences.edit().apply {
            putBoolean(NOTIFY_ME, notifyMe)
        }.apply()
    }

    fun getNotifyMe(): Boolean = preferences.getBoolean(NOTIFY_ME, false)

    fun setIsSeen(isSeen: Boolean) {
        preferences.edit().apply {
            putBoolean(IS_SEEN, isSeen)
        }.apply()
    }

    fun getIsSeen(): Boolean = preferences.getBoolean(IS_SEEN, false)

    fun clearAll() {
        preferences.edit().clear().apply()
    }
}