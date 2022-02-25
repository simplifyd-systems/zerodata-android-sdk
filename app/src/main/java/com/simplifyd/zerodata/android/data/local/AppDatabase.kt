package com.simplifyd.zerodata.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.simplifyd.zerodata.android.data.model.User

@Database(entities = [User::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}