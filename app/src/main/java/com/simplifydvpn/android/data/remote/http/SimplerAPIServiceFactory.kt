package com.simplifydvpn.android.data.remote.http

import com.simplifydvpn.android.BuildConfig
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SimplerAPIServiceFactory {

    val simplerApiService: APIService
        get() {
            val dispatcher = makeDispatcher()
            val loggingInterceptor = makeLoggingInterceptor()
            val okHttpClient = makeSimplerOkHttpClient(loggingInterceptor, dispatcher)
            val retrofit = makeRetrofit(okHttpClient)

            return retrofit.create(APIService::class.java)
        }


    private fun makeDispatcher(): Dispatcher {
        val dispatcher = Dispatcher()
        dispatcher.maxRequestsPerHost = 10
        return dispatcher
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private fun makeSimplerOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        dispatcher: Dispatcher
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(AuthInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .dispatcher(dispatcher)

        return httpClientBuilder.build()
    }


    private fun makeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}