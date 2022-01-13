package com.simplifydvpn.android.data.remote.http

import com.simplifydvpn.android.data.model.DashboardData
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.data.model.User
import com.simplifydvpn.android.data.model.apiresponse.LoginResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface APIService {

    @POST("customer/auth/login2")
    fun executeLogin(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<LoginResponse>

    @POST("customer/auth/login2")
    suspend fun login(@Body body: Map<String, @JvmSuppressWildcards Any>): LoginResponse

    @POST("customer/auth/register")
    suspend fun signUp(@Body body: Map<String, @JvmSuppressWildcards Any>): LoginResponse

    @PATCH("customer/subscriptions/protect")
    suspend fun protectUser(): User

    @PATCH("customer/subscriptions/unprotect")
    suspend fun unProtectUser(): User

    @GET("customer/rules")
    suspend fun getCustomerRules(): List<Rule>

    @GET("customer/constants/domains/categories")
    suspend fun getAllCategories(): HashMap<String, String>

    @DELETE("customer/rules/{ruleId}")
    suspend fun deleteRule(@Path("ruleId") ruleId: String)

    @POST("customer/rules")
    suspend fun addRule(@Body body: Map<String, @JvmSuppressWildcards Any>): Rule

    @GET("customer/analytics")
    suspend fun getDashboardData(): DashboardData

}