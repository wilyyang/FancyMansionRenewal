package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.ReadMode

data class ConfigModel(
    val id: Long,
    val version: Long = 0L,
    val createTime: Long,
    val editTime: Long,
    val readMode: ReadMode = ReadMode.EDIT,

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
