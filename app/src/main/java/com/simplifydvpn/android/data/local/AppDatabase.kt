package com.simplifydvpn.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.simplifydvpn.android.data.model.User

@Database(entities = [User::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}