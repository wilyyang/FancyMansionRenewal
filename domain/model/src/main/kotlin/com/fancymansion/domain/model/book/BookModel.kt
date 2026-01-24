package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.ReadMode

data class BookInfoModel(
    val id: String,
    val introduce: IntroduceModel,
    val editor: EditorModel
)

data class IntroduceModel(
    val title: String = "",
    val coverList: List<String> = listOf(),
    val keywordList: List<KeywordModel> = listOf(),
    val description: String = ""
)

data class EditorModel(
    val editorId: String = "",
    val editorName: String = "",
    val editorEmail: String = ""
)

data class EpisodeInfoModel(
    val id: String,
    val bookId: String,
    val title: String,
    val pageCount: Int,
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
    val updateTime: Long = 0L,
    val readMode: ReadMode = ReadMode.EDIT
)