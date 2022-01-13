package com.simplifydvpn.android.data.remote.grpc

import com.simplifydvpn.android.BuildConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object GRPCChannelFactory {

    val grpcChannel: ManagedChannel
        get() {
            val channel = ManagedChannelBuilder
                .forAddress("edge2.simplifyd.net", 30000)
                .usePlaintext()
                .build()

            return channel
        }
}