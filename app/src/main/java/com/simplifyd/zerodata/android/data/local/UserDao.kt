package com.simplifyd.zerodata.android.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simplifyd.zerodata.android.data.model.User

@Dao
interface UserDao {
//
//    @Query("SELECT * FROM user LIMIT 1")
//    fun getUser(): LiveData<User>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun saveUser(user: User)
//
//    @Query("SELECT is_protected FROM user LIMIT 1")
//    fun getProtectMe(): LiveData<Boolean>

//    @Query("UPDATE user SET is_protected = :protectMe")
//    suspend fun setProtectMe(protectMe: Boolean)
}