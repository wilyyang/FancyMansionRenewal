package com.fancymansion.data.datasource.network.base.model

abstract class AbstractResponse<T : Any> {
    val data : T? = null
    val status : Int? = null
    val message : String? = null
}

data class BaseResponse<T : Any>(
    val access_token : String? = null
): AbstractResponse<T>()