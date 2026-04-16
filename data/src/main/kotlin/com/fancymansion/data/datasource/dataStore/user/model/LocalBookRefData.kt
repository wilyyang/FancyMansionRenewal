package com.fancymansion.data.datasource.dataStore.user.model

import com.fancymansion.domain.model.user.BookRefModel

data class LocalBookRefData(
    val bookId: String,
    val version: Int
)

fun LocalBookRefData.asModel() = BookRefModel(
    bookId = bookId,
    version = version
)

fun BookRefModel.asLocalData() = LocalBookRefData(
    bookId = bookId,
    version = version
)