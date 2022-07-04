package com.simplifyd.zerodata.data.remote.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object GRPCChannelFactory {

    val grpcChannel: ManagedChannel
        get() {
            return ManagedChannelBuilder
                .forAddress("staging.api-rpc.simplifyd.net", 30000)
//                .forAddress("edge1.api-rpc.simplifyd.net", 30000)
                .usePlaintext()
                .build()
        }
}