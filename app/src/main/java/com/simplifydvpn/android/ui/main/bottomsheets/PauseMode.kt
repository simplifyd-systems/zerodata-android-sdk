package com.simplifydvpn.android.ui.main.bottomsheets

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class PauseMode : Parcelable {
    PAUSE_FOR_FIFTEEN_MINUTES,
    PAUSE_FOR_ONE_HOUR,
    DISABLE,
    CANCEL
}