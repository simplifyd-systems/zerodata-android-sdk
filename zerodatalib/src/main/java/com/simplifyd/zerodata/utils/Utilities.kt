package com.simplifyd.zerodata.utils

import io.grpc.StatusRuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException

sealed class Status<out T : Any> {

    data class Success<out T : Any>(val data: T) : Status<T>()

    data class Error(val error: Throwable) : Status<Nothing>()
}

fun handleError(exception: Throwable): Throwable {
    return if (exception is SocketTimeoutException || exception is ConnectException) {
        Throwable("Please check your internet connection and retry.")
    } else if (exception is StatusRuntimeException) {
        if (exception.status.code == io.grpc.Status.Code.PERMISSION_DENIED) {
            Throwable("Token expired")
        } else {
            Throwable("There was an error handling your request, please retry.")
        }
    } else {
        Throwable("There was an error handling your request, please retry.")
    }
}


