package com.simplifyd.zerodata.android.data.model.apiresponse

import com.simplifyd.zerodata.android.data.model.User

data class LoginResponse(
    val user: User,
    val jwt: String
)