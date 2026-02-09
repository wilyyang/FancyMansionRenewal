package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.homeBook.BookHomeModel

const val NOT_ASSIGN_PUBLISHED_ID = "NOT_ASSIGN_PUBLISHED_ID"
const val NOT_ASSIGN_PUBLISHED_AT = 0L
const val NOT_ASSIGN_UPDATED_AT = 0L

data class BookInfoData(
    val bookId: String,
    val publishInfo: PublishInfoData,
    val introduce: IntroduceData,
    val editor: EditorData,
) {
    companion object Fields {
        const val BOOK_ID = "bookId"
        const val PUBLISH_INFO = "publishInfo"
        const val INTRODUCE = "introduce"
        const val EDITOR = "editor"
    }
}

fun BookInfoModel.asData() = BookInfoData(
    bookId = id,
    publishInfo = PublishInfoData(
        publishedId = NOT_ASSIGN_PUBLISHED_ID,
        publishedAt = NOT_ASSIGN_PUBLISHED_AT,
        updatedAt = NOT_ASSIGN_UPDATED_AT,
        version = 0,
        likeCount = 0
    ),
    introduce = introduce.asData(),
    editor = editor.asData()
)

fun BookInfoData.asHomeModel(keywordMap: Map<Long, KeywordModel>) = BookHomeModel(
    id = bookId,
    publishInfo = publishInfo.asModel(),
    introduce = introduce.asHomeModel(keywordMap),
    editor = editor.asHomeModel()
)