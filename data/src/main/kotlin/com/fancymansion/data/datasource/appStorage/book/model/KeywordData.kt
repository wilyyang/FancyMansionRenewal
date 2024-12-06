package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.domain.model.book.KeywordModel

data class KeywordData(val id: Long, val category: String, val name: String)

fun KeywordData.asModel() = KeywordModel(
    id = id,
    category = category,
    name = name
)

fun KeywordModel.asData() = KeywordData(
    id = id,
    category = category,
    name = name
)