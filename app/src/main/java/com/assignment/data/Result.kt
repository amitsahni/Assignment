package com.assignment.data

import com.assignment.data.bean.ErrorResponse
import com.assignment.extension.fromJson

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: ErrorResponse) : Result<Nothing>()
    data class Exception(val e: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            is Loading -> "Loading"
            is Exception -> "Exception[expection=$e]"
        }
    }
}

/**
 * `true` if [Result] is of episodeType [Success] & holds non-null [Success.data].
 */
val Result<*>.isSucceeded
    get() = this is Result.Success && data != null


fun <T> Result<T>.success(): T? {
    return when (this) {
        is Result.Success -> this.data
        else -> null
    }
}

fun <T> Result<T>.error(): ErrorResponse? {
    if (this is Result.Error) {
        return error
    }
    return null
}

fun <T> Response<T>.toResult(): Result<T> {
    return when (this) {
        is Response.Success -> Result.Success(data)
        is Response.Error -> Result.Error(data.fromJson())
        is Response.Exception -> Result.Exception(exception)
    }
}