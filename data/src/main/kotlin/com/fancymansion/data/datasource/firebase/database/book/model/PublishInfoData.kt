package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.homeBook.PublishInfoModel

data class PublishInfoData(
    val publishedId: String,
    val publishedAt: Long,
    val updatedAt: Long,
    val version: Int,
    val likeCount: Int
) {
    companion object Fields {
        const val PUBLISHED_ID = "publishedId"
        const val PUBLISHED_AT = "publishedAt"
        const val UPDATED_AT = "updatedAt"
        const val VERSION = "version"
        const val LIKE_COUNT = "likeCount"
    }
}

fun PublishInfoModel.asData() = PublishInfoData(
    publishedId = publishedId,
    publishedAt = publishedAt,
    updatedAt = updatedAt,
    version = version,
    likeCount = likeCount
)


fun PublishInfoData.asModel() = PublishInfoModel(
    publishedId = publishedId,
    publishedAt = publishedAt,
    updatedAt = updatedAt,
    version = version,
    likeCount = likeCount
)