package com.fancymansion.domain.usecase

import com.fancymansion.core.common.wrapper.ApiError
import com.fancymansion.core.common.wrapper.ApiResult
import com.fancymansion.core.common.wrapper.ClientError
import com.fancymansion.core.common.wrapper.ServerError
import com.fancymansion.core.common.wrapper.StatusError
import com.fancymansion.core.common.wrapper.Success
import com.fancymansion.core.common.throwable.ApiNetworkException
import com.fancymansion.core.common.throwable.ApiResultStatusException
import com.fancymansion.core.common.throwable.RequestInternetCheckException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlin.reflect.KClass

suspend fun <T> ApiResult<T>.getSuccessOrCancel(kClass: KClass<*>): ApiResult<T> = coroutineScope {
    if(this@getSuccessOrCancel !is Success){
        when(this@getSuccessOrCancel){
            is ServerError -> cancel(cause = ApiNetworkException(result = this@getSuccessOrCancel.copy(id = kClass.simpleName)))
            is ClientError -> cancel(cause = ApiNetworkException(result = this@getSuccessOrCancel.copy(id = kClass.simpleName)))
            is StatusError -> cancel(cause = ApiResultStatusException(result = this@getSuccessOrCancel.copy(id = kClass.simpleName)))
            is ApiError -> cancel(cause = RequestInternetCheckException(result = this@getSuccessOrCancel.copy(id = kClass.simpleName)))
            else -> cancel(cause = RequestInternetCheckException(result = ApiError(id = kClass.simpleName)))
        }
    }
    this@getSuccessOrCancel
}