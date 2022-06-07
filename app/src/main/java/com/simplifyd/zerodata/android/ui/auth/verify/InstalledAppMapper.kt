package com.simplifyd.zerodata.android.ui.auth.verify

import com.simplifyd.zerodata.android.data.model.InstalledAppData
import com.simplifyd.zerodata.android.utils.UIModelMapper
import zerodata_api.ApiRpc

class InstalledAppMapper: UIModelMapper<ApiRpc.InstalledApp, InstalledAppData>() {
    override fun mapToUI(entity: ApiRpc.InstalledApp): InstalledAppData {

        TODO("Not yet implemented")
    }

    override fun mapFromUI(model: InstalledAppData): ApiRpc.InstalledApp {
        return with(model){
            ApiRpc.InstalledApp.newBuilder().setName(model.app_name ?: "").setPackageName(model.app_package_name).build()
        }

    }

}