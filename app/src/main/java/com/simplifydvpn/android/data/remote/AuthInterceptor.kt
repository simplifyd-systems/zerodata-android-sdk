package com.simplifydvpn.android.data.remote

import android.util.Log
import com.simplifydvpn.android.data.local.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        if (!PreferenceManager.getToken().isNullOrEmpty()) {
            Log.d("TOKE",  PreferenceManager.getToken())
            requestBuilder.addHeader("Authorization", "Bearer " + PreferenceManager.getToken())
        }

        return chain.proceed(requestBuilder.build())
    }
}
