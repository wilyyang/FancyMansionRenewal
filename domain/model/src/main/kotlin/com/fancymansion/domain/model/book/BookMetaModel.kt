package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.INIT_DOWNLOAD_AT
import com.fancymansion.core.common.const.EditorPublishStatus

data class BookMetaModel(
    val status: EditorPublishStatus,
    val publishedAt: Long,
    val updatedAt: Long,
    val downloadAt: Long = INIT_DOWNLOAD_AT,
    val version: Int
)