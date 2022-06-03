package com.simplifyd.zerodata.android.ui.main.catalogue.mapper

import com.simplifyd.zerodata.android.ui.main.catalogue.ListedApp
import com.simplifyd.zerodata.android.utils.UIModelMapper
import zerodata_api.ApiRpc

class ListedAppMapper: UIModelMapper<ApiRpc.ListedApps.ListedApp, ListedApp>() {
    override fun mapToUI(entity: ApiRpc.ListedApps.ListedApp): ListedApp {
        return with (entity){
            ListedApp(entity.info, entity.name, entity.url, entity.category, entity.image)
        }
    }
}