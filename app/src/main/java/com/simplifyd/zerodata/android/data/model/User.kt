package com.simplifyd.zerodata.android.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey
    val id: String,
    val fname: String,
    val lname: String,
    val email: String,
    val mobile: String,
    val is_protected: Boolean?,
    val created_at: String,
    val organization: String
) : Parcelable {

    val customer get() = "$fname $lname"
}