package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_VERSION
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.domain.model.book.BookMetaModel

data class BookMetaData(
    val status: PublishStatus = PublishStatus.UNPUBLISHED,
    val publishedAt: Long = INIT_PUBLISHED_AT,
    val version: Int = INIT_VERSION
)

fun BookMetaData.asModel() = BookMetaModel(
    status = status,
    publishedAt = publishedAt,
    version = version
)

fun BookMetaModel.asData() = BookMetaData(
    status = status,
    publishedAt = publishedAt,
    version = version
)