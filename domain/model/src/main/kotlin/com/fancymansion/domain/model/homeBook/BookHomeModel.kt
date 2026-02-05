package com.fancymansion.domain.model.homeBook

import com.fancymansion.domain.model.book.KeywordModel

data class BookHomeModel(
    val id: String,
    val publishInfo: PublishInfoModel,
    val introduce: IntroduceHomeModel,
    val editor: EditorHomeModel
)

data class PublishInfoModel(
    val publishedId: String,
    val publishedAt: Long,
    val version: Int,
    val likeCount: Int
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