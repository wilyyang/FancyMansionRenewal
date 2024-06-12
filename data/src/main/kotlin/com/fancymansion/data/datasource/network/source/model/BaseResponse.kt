package com.fancymansion.data.datasource.network.source.model

import com.fancymansion.domain.model.MessageModel

abstract class AbstractResponse<T : Any> {
    val data : T? = null
    val status : Int? = null
    val message : String? = null
}

data class BaseResponse<T : Any>(
    val access_token : String? = null
): AbstractResponse<T>()

class MessageResponse: AbstractResponse<String>()

fun MessageResponse.asModel() = MessageModel(
    message = message,
    status = status
)