package com.simplifydvpn.android.data.remote

import android.util.Log
import com.google.gson.Gson
import com.simplifydvpn.android.data.local.PreferenceManager
import okhttp3.*

class AccessTokenAuthenticator  constructor(
    private val tokenRefresher: TokenRefresher = TokenRefresher(Gson()),
    private val preferenceManager: PreferenceManager = PreferenceManager,
    ) : Interceptor {

    private var countOfRetry: Int = 0


    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response?.code() == 403 && countOfRetry < 4) {
            // We need to have a token in order to refresh it.
            Log.d(AccessTokenAuthenticator::class.java.simpleName, "ReAuthenticating token")

            val email = PreferenceManager.getAccountLogin()
            val password = PreferenceManager.getAccountPassword()

            if (email != null && password != null) {
                val newToken = tokenRefresher.refreshToken(email, password)
                newToken?.let {
                    preferenceManager.saveToken(it)
                }
            }

            countOfRetry++

            Log.d(
                AccessTokenAuthenticator::class.java.simpleName,
                "Re authenticating token success on retry $countOfRetry"
            )
            response.close()
            return chain.proceed(response.request().newBuilder()
                .header("Authorization", "Bearer " + preferenceManager.getToken())
                .build())
        } else {
            countOfRetry = 0
            Log.d(AccessTokenAuthenticator::class.java.simpleName, "No need to refresh token")
            response.close()
            return chain.proceed(chain.request())
            //response.newBuilder().body(ResponseBody.create(contentType, bodyString)).build();
        }
    }


//    private fun getErrorMessageIfExists(response: Response?): String? {
//        var errorMessage: String? = null
//        try {
//            errorMessage = getErrorMessage(response?.body())
//        } catch (ex: Exception) {
//            Log.d(AccessTokenAuthenticator::class.java.simpleName, "Failed re auth token", ex)
//        }
//        return errorMessage
//    }


}
