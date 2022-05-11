package com.simplifyd.zerodata.android.ui.main.notification.mapper

import com.simplifyd.zerodata.android.data.model.NotificationData
import pb.ApiRpc

class NotificationMapper:UIModelMapper<ApiRpc.Notification, NotificationData>() {
    override fun mapToUI(entity: ApiRpc.Notification): NotificationData {
        return with (entity){
            NotificationData("", entity.title, entity.body, "")
        }
    }
}