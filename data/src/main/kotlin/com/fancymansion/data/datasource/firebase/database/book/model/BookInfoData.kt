package com.fancymansion.data.datasource.firebase.database.book.model

import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.homeBook.BookHomeModel

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

fun BookInfoData.asHomeModel(keywordMap: Map<Long, KeywordModel>) = BookHomeModel(
    id = bookId,
    publishedId = publishedId,
    introduce = introduce.asHomeModel(keywordMap),
    editor = editor.asHomeModel()
)