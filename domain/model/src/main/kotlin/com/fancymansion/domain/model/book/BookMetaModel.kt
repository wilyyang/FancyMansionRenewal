package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.PublishStatus

data class BookMetaModel(
    val status: PublishStatus,
    val publishedAt: Long,
    val updatedAt: Long,
    val version: Int
)