package com.simplifydvpn.android.data.repo

import com.simplifydvpn.android.data.local.DatabaseManager
import com.simplifydvpn.android.data.remote.http.APIServiceFactory

abstract class BaseRepository {
    protected val apiService = APIServiceFactory.apiService
    protected val database = DatabaseManager.database

}