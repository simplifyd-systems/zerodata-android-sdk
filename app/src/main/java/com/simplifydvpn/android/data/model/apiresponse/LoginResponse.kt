package com.simplifydvpn.android.data.model.apiresponse

import com.simplifydvpn.android.data.model.User

data class LoginResponse(
    val user: User,
    val jwt: String
)