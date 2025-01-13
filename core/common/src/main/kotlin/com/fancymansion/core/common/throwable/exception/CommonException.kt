package com.fancymansion.core.common.throwable.exception

import com.fancymansion.core.common.const.EpisodeRef
import kotlin.coroutines.cancellation.CancellationException
import com.fancymansion.core.common.wrapper.Fail

class ScreenDataInitFailException(
    val result : Fail? = null,
    val title : String? = null,
    override val message : String = "Screen Data Init Fail Exception" + if(result != null) ("\nCode : ${result.code} \nMessage : ${result.message}") else ""
) : CancellationException()

class WebViewException(
    val result : Fail,
    override val message : String? = null
) : CancellationException()

/**
 * File Load Exception
 */
class LoadPageException(
    message: String,
    val episodeRef: EpisodeRef,
    val pageId: Long,
    cause: Throwable? = null
) : Exception(message, cause)