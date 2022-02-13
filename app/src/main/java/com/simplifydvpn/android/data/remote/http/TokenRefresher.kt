package com.simplifydvpn.android.data.remote.http

import android.util.Log
import com.google.gson.Gson

class TokenRefresher constructor(var gson: Gson) {

    fun refreshToken(password: String, email: String): String? {
        val hashMap = HashMap<String, Any>()
        hashMap["password"] = password
        hashMap["email"] = email

        val data = SimplerAPIServiceFactory.simplerApiService.executeLogin(hashMap).execute().body()
        Log.d(TokenRefresher::class.java.simpleName, data.toString())
        return data?.jwt
    }

}