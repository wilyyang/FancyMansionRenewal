package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.INIT_DOWNLOAD_AT
import com.fancymansion.core.common.const.PublishStatus

data class BookMetaModel(
    val status: PublishStatus,
    val publishedAt: Long,
    val updatedAt: Long,
    val downloadAt: Long = INIT_DOWNLOAD_AT,
    val version: Int
)