package com.fancymansion.core.common.throwable.exception

import com.fancymansion.core.common.const.EpisodeRef

/**
 * File Load Exception
 */
class LoadPageException(
    message: String,
    val episodeRef: EpisodeRef,
    val pageId: Long,
    cause: Throwable? = null
) : Exception(message, cause)