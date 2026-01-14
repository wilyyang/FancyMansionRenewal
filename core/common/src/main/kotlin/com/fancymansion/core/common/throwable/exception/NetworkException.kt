package com.fancymansion.core.common.throwable.exception

import com.fancymansion.core.common.wrapper.Fail

class RequestInternetCheckException(
    val result : Fail? = null
) : Exception()

class NetworkUnknownException(
    val result : Fail? = null
) : Exception()

class InternetDisconnectException(
    val result : Fail? = null
) : Exception()