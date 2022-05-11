package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.data.model.NotificationData
import com.simplifyd.zerodata.android.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError
import pb.ApiRpc
import pb.EdgeGrpc

class NotificationRepository: BaseRepository() {

     fun getNotifications(): Status<List<ApiRpc.Notification>>{
        return try {
            val notificationRq = ApiRpc.Empty.newBuilder().build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val response = blockingStub.getNotifications(notificationRq)

            Status.Success(response.notificationsList)

        }catch (error: Throwable) {
            Status.Error(handleError(error))
        }
    }
}