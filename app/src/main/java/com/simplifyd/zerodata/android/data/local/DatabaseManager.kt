package com.simplifyd.zerodata.android.data.local

import android.content.Context
import androidx.room.Room

object DatabaseManager {

    private const val DATABASE_NAME = "simplify-dns-db"

    lateinit var context: Context

    val database: AppDatabase
        get() = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
}