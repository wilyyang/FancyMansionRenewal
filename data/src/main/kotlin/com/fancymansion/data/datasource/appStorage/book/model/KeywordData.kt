package com.fancymansion.data.datasource.appStorage.book.model

import com.fancymansion.domain.model.book.KeywordModel

data class KeywordData(val name: String)

fun KeywordData.asModel() = KeywordModel(
    name = name
)

fun KeywordModel.asData() = KeywordData(
    name = name
)