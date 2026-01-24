package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.KeywordModel

data class BookInfoData(
    val bookId: String,
    val publishedId: String?,
    val introduce: IntroduceData,
    val editor: EditorData,
) {
    companion object Fields {
        const val BOOK_ID = "bookId"
        const val PUBLISHED_ID = "publishedId"
        const val INTRODUCE = "introduce"
        const val EDITOR = "editor"
    }
}

fun BookInfoModel.asData() = BookInfoData(
    bookId = id,
    publishedId = publishedId,
    introduce = introduce.asData(),
    editor = editor.asData()
)

fun BookInfoData.asModel(keywordMap: Map<Long, KeywordModel>) = BookInfoModel(
    id = bookId,
    publishedId = publishedId,
    introduce = introduce.asModel(keywordMap),
    editor = editor.asModel()
)