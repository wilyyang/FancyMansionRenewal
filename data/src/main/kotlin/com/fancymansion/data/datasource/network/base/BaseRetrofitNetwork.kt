package com.fancymansion.data.datasource.network.base

import com.fancymansion.core.common.const.STATUS_SUCCESS
import retrofit2.Response
import com.fancymansion.core.common.wrapper.ClientError
import com.fancymansion.core.common.wrapper.ApiResult
import com.fancymansion.core.common.wrapper.ServerError
import com.fancymansion.core.common.wrapper.Success
import com.fancymansion.core.common.wrapper.ApiError
import com.fancymansion.core.common.wrapper.StatusError
import com.fancymansion.data.datasource.network.base.model.AbstractResponse

abstract class BaseRetrofitNetwork {
    suspend fun <A : AbstractResponse<D>, D, R> callApi(
        api : suspend () -> Response<A>,
        mapping : (Response<A>) -> R
    ) : ApiResult<R> {
        return try {
            api().let { result ->
                when (result.code()) {
                    in 200 until 300 -> {
                        val status : Int? = result.body()?.status
                        if(status != null && status != STATUS_SUCCESS){
                            StatusError(code = result.code(), message = result.body()?.message, status = status)
                        }else{
                            Success(data = mapping(result))
                        }
                    }
                    in 400 until 500 -> ClientError(result.code(), result.message())
                    in 500 until 600-> ServerError(result.code(), result.message())
                    else -> ApiError(code = result.code(), message = "Unknown Error - ${result.errorBody().toString()}")
                }
            }
        } catch (t : Throwable) {
            ApiError(message = t.message)
        }
    }
}