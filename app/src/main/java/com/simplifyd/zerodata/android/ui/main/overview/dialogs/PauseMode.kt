package com.simplifyd.zerodata.android.ui.main.overview.dialogs

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class PauseMode : Parcelable {
    PAUSE_FOR_FIFTEEN_MINUTES,
    PAUSE_FOR_ONE_HOUR,
    DISABLE,
    CANCEL
}