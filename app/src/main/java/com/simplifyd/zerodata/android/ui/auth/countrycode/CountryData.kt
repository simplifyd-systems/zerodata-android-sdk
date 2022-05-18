package com.simplifyd.zerodata.android.ui.auth.countrycode

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryData(
    val id: Int = 0,
    @StringRes val countryName: Int,
    @StringRes val countryCode: Int,
    @DrawableRes val flagImageResourceId: Int
): Parcelable