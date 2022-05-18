package com.simplifyd.zerodata.android.ui.main.catalogue

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ListedApp(
    val id: Int,
    val title: String,
    val url: String,
    @StringRes val category: Int,
    @DrawableRes val imageResourceId: Int
)
