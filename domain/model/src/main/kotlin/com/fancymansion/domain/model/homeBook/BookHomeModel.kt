package com.fancymansion.domain.model.homeBook

import com.fancymansion.domain.model.book.KeywordModel

data class BookHomeModel(
    val id: String,
    val publishedId: String,
    val publishedAt: Long,
    val introduce: IntroduceHomeModel,
    val editor: EditorHomeModel
)

data class IntroduceHomeModel(
    val title: String = "",
    val coverList: List<String> = listOf(),
    val keywordList: List<KeywordModel> = listOf(),
    val description: String = ""
)

data class EditorHomeModel(
    val editorId: String = "",
    val editorName: String = "",
    val editorEmail: String = ""
)