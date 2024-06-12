package com.fancymansion.core.common.wrapper

sealed interface ApiResult<out T>
data class Success<out T>(val data: T) : ApiResult<T>

sealed interface Fail : ApiResult<Nothing> {
    val code: Int?
    val message: String?
    val id: String?
}

data class ApiError(override val code: Int? = null, override val message: String? = null, override val id: String? = null) :
    Fail

sealed interface NetworkError : Fail
data class ServerError(override val code: Int? = null, override val message: String? = null, override val id: String? = null) :
    NetworkError

data class ClientError(override val code: Int? = null, override val message: String? = null, override val id: String? = null) :
    NetworkError

data class StatusError(override val code: Int? = null, override val message: String? = null, override val id: String? = null, val status: Int) :
    NetworkError