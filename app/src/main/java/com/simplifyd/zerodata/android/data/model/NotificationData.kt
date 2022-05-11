package com.simplifyd.zerodata.android.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationData(var id: String?,
                            val notification_name: String?,
                            val notification_content: String?,
                            val notification_date: String?): Parcelable
