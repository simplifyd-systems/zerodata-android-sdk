package com.simplifyd.zerodata.android.ui.intro

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Intro(
    val id: Int,
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int,
    @DrawableRes val imageResourceId: Int
)