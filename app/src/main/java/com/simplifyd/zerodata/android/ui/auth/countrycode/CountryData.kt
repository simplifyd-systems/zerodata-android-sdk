package com.simplifyd.zerodata.android.ui.auth.countrycode

import androidx.annotation.DrawableRes

data class CountryData(
    val id: Int,
    val countryName: String,
    val countryCode: Int,
    @DrawableRes val flagImageResourceId: Int
)