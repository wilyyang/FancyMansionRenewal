package com.fancymansion.core.common.throwable

import kotlin.coroutines.cancellation.CancellationException
import com.fancymansion.core.common.wrapper.Fail
import com.fancymansion.core.common.wrapper.StatusError

class ScreenDataInitFailException(
    val result : Fail? = null,
    val title : String? = null,
    override val message : String = "Screen Data Init Fail Exception" + if(result != null) ("\nCode : ${result.code} \nMessage : ${result.message}") else ""
) : CancellationException()

class RequestInternetCheckException(
    val result : Fail
) : CancellationException()
abstract class ApiCallException(open val result : Fail) : CancellationException()

class ApiNetworkException(
    override val result : Fail,
    val title : String? = null,
    override val message : String = "Api Network Exception \nCode : ${result.code} \nMessage : ${result.message}"
) : ApiCallException(result)

class ApiUnknownException(
    override val result : Fail,
    override val message : String = "Api Unknown Exception \nMessage : ${result.message}"
) : ApiCallException(result)

class ApiResultStatusException(
    override val result : StatusError,
    override val message : String = "Api Status Exception \nMessage : ${result.message} \nstatus : ${result.status}",
) : ApiCallException(result)

class InternetDisconnectException(
    val result : Fail,
) : CancellationException()

class WebViewException(
    val result : Fail,
    override val message : String? = null
) : CancellationException()