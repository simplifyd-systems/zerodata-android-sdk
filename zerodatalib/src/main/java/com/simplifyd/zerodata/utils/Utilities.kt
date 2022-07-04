package com.simplifyd.zerodata.utils

import java.net.ConnectException
import java.net.SocketTimeoutException

sealed class Status<out T : Any> {

    data class Success<out T : Any>(val data: T) : Status<T>()

    data class Error(val error: Throwable) : Status<Nothing>()
}

fun handleError(exception: Throwable): Throwable {
    return if (exception is SocketTimeoutException || exception is ConnectException) {
        Throwable("Please check your internet connection and retry.")
    } else {
        Throwable("There was an error handling your request, please retry.")
    }
}


