package com.fancymansion.core.common.throwable.exception

import com.fancymansion.core.common.wrapper.Fail
import com.fancymansion.core.common.wrapper.StatusError
import kotlin.coroutines.cancellation.CancellationException

abstract class ApiCallException(open val result : Fail) : CancellationException()

class ApiNetworkException(
    override val result : Fail,
    val title : String? = null,
    override val message : String = "Api Network Exception \nCode : ${result.code} \nMessage : ${result.message}"
) : ApiCallException(result)

class ApiResultStatusException(
    override val result : StatusError,
    override val message : String = "Api Status Exception \nMessage : ${result.message} \nstatus : ${result.status}",
) : ApiCallException(result)

class RequestInternetCheckException(
    val result : Fail
) : CancellationException()

class ApiUnknownException(
    override val result : Fail,
    override val message : String = "Api Unknown Exception \nMessage : ${result.message}"
) : ApiCallException(result)

class InternetDisconnectException(
    val result : Fail,
) : CancellationException()