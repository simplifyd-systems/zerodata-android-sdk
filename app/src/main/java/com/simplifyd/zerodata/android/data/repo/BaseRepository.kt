package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.DatabaseManager
import com.simplifyd.zerodata.android.data.remote.http.APIServiceFactory

abstract class BaseRepository {
    protected val apiService = APIServiceFactory.apiService
    protected val database = DatabaseManager.database

}