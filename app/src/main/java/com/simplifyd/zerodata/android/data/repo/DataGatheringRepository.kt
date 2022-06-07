package com.simplifyd.zerodata.android.data.repo

import com.simplifyd.zerodata.android.data.local.PreferenceManager
import com.simplifyd.zerodata.android.data.remote.grpc.GRPCChannelFactory
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.handleError
import zerodata_api.ApiRpc
import zerodata_api.EdgeGrpc

class DataGatheringRepository: BaseRepository() {

    fun postDeviceModel(manufacturer: String, brand: String, model:String ): Status<Unit>{
        return try{
            val deviceModelRq = ApiRpc.DeviceInfo.newBuilder().setManufacturer(manufacturer).setBrand(brand).setModel(model).build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val  response = blockingStub.postDeviceInfo(deviceModelRq)
            Status.Success(Unit)
        }catch (error: Throwable){
            Status.Error(handleError(error))
        }
    }

    fun postInstalledApps(installedApps: List<ApiRpc.InstalledApp>): Status<Unit>{
        return try{
            val installedAppsRq = ApiRpc.InstalledApps.newBuilder().addAllInstalledApps(installedApps).build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val response = blockingStub.postInstalledApps(installedAppsRq)
            Status.Success(Unit)
        } catch (error:Throwable){
            Status.Error(handleError(error))
        }
    }

    fun postInstalledZerodataOn(name: String, package_name: String, installedTime: Long): Status<Unit>{
        return try{
            val installedAppRq = ApiRpc.InstalledApp.newBuilder().setName(name).setPackageName(package_name).setInstallTime(installedTime).build()
            val token = PreferenceManager.getToken()
            val creds = AuthenticationCallCredentials(token)
            val blockingStub = EdgeGrpc.newBlockingStub(GRPCChannelFactory.grpcChannel).withCallCredentials(creds)
            val response = blockingStub.postInstalledAppUsingZerodata(installedAppRq)
            Status.Success(Unit)
        }catch(error:Throwable){
            Status.Error(handleError(error))
        }
    }
}