package com.simplifyd.zerodata.android.data.remote.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object GRPCChannelFactory {

    val grpcChannel: ManagedChannel
        get() {
            val channel = ManagedChannelBuilder
                .forAddress("edge1.api-rpc.simplifyd.net", 30000)
                .usePlaintext()
                .build()

            return channel
        }
}