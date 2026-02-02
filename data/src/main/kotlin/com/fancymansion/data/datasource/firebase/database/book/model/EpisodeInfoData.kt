package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.homeBook.EpisodeHomeModel

data class EpisodeInfoData(
    val episodeId: String,
    val bookId: String,
    val title: String,
    val pageCount: Int,
    val version: Long,
    val createTime: Long,
    val editTime: Long
) {
    companion object Fields {
        const val EPISODE_ID = "episodeId"
        const val BOOK_ID = "bookId"
        const val TITLE = "title"
        const val PAGE_COUNT = "pageCount"
        const val VERSION = "version"
        const val CREATE_TIME = "createTime"
        const val EDIT_TIME = "editTime"
    }
}

fun EpisodeInfoModel.asData(): EpisodeInfoData = EpisodeInfoData(
    episodeId = id,
    bookId = bookId,
    title = title,
    pageCount = pageCount,
    version = version,
    createTime = createTime,
    editTime = editTime
)

fun EpisodeInfoData.asHomeModel(): EpisodeHomeModel = EpisodeHomeModel(
    id = episodeId,
    bookId = bookId,
    title = title,
    pageCount = pageCount,
    version = version,
    createTime = createTime,
    editTime = editTime
)