package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.ReadMode

data class Config(
    val id: Long,
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
    val readMode: ReadMode = ReadMode.EDIT,

    val introduce: Introduce,
    val editor: Editor
)

data class Introduce(
    val title: String = "",
    val coverList: List<String> = listOf(),
    val keywordList: List<Keyword> = listOf(),
    val description: String = ""
)

data class Editor(
    val editorId: String = "",
    val editorName: String = "",
    val editorEmail: String = ""
)
