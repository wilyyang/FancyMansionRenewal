package com.fancymansion.data.datasource.firebase.database.book.model.result

import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData
import com.fancymansion.data.datasource.firebase.database.book.model.asHomeModel
import com.fancymansion.domain.model.homeBook.result.LoadBookResult

sealed class LoadBookDataResult {
    data class Success(val data: HomeBookItemData) : LoadBookDataResult()
    object Withdrawn : LoadBookDataResult()
    object NotFound : LoadBookDataResult()
}

fun LoadBookDataResult.toDomain(): LoadBookResult {
    return when (this) {
        is LoadBookDataResult.Success -> {
            LoadBookResult.Success(
                model = data.asHomeModel()
            )
        }
        LoadBookDataResult.Withdrawn -> LoadBookResult.Withdrawn
        LoadBookDataResult.NotFound -> LoadBookResult.NotFound
    }
}