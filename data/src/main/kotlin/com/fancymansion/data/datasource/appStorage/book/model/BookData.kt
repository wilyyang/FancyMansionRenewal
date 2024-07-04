package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.domain.model.book.ConfigModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.IntroduceModel

data class ConfigData(
    val id: Long,
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
    val readMode: ReadMode = ReadMode.EDIT,

    val introduce: IntroduceData,
    val editor: EditorData
)

data class IntroduceData(
    val title: String = "",
    val coverList: List<String> = listOf(),
    val keywordList: List<KeywordData> = listOf(),
    val description: String = ""
)

data class EditorData(
    val editorId: String = "",
    val editorName: String = "",
    val editorEmail: String = ""
)

fun ConfigData.asModel() = ConfigModel(
    id = id,
    version = version,
    createTime = createTime,
    editTime = editTime,
    readMode = readMode,
    introduce = introduce.asModel(),
    editor = editor.asModel()
)

fun ConfigModel.asData() = ConfigData(
    id = id,
    version = version,
    createTime = createTime,
    editTime = editTime,
    readMode = readMode,
    introduce = introduce.asData(),
    editor = editor.asData()
)

fun IntroduceData.asModel() = IntroduceModel(
    title = title,
    coverList = coverList,
    keywordList = keywordList.map { it.asModel() },
    description = description
)

fun IntroduceModel.asData() = IntroduceData(
    title = title,
    coverList = coverList,
    keywordList = keywordList.map { it.asData() },
    description = description
)

fun EditorData.asModel() = EditorModel(
    editorId = editorId,
    editorName = editorName,
    editorEmail = editorEmail
)

fun EditorModel.asData() = EditorData(
    editorId = editorId,
    editorName = editorName,
    editorEmail = editorEmail
)