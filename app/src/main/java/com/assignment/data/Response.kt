package com.assignment.data


sealed class Response<out T> {

    data class Success<out T>(val data: T?) : Response<T>()
    data class Error(val data: String?) : Response<Nothing>()
    //data class Exception(val exception: Throwable) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[data=$data]"
            //is Exception -> "Exception[error=${exception.message}]"
        }
    }
}

/**
 * `true` if [Result] is of episodeType [Success] & holds non-null [Success.data].
 */
val Response<*>.succeeded
    get() = this is Response.Success && data != null

val Response<*>.error
    get() = this is Response.Error && data != null
