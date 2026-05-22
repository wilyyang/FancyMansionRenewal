package com.fancymansion.data.datasource.firebase.database.book.model.result

import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData
import com.fancymansion.data.datasource.firebase.database.book.model.asHomeModel
import com.fancymansion.domain.model.homeBook.result.BookQueryModel
import com.fancymansion.domain.model.homeBook.result.BookQueryResult

data class BookQueryData(
    val bookList: List<HomeBookItemData>,
    val nextBookId: String?
)

fun BookQueryData.toDomain() = BookQueryModel(
    bookList = bookList.map { it.asHomeModel() },
    nextBookId = nextBookId
)

sealed class BookQueryDataResult {
    data class Success(val data: BookQueryData) : BookQueryDataResult()
    object InvalidSearch : BookQueryDataResult()
    object CursorNotExist : BookQueryDataResult()
    object NotFoundBook : BookQueryDataResult()
}

fun BookQueryDataResult.toDomain(): BookQueryResult {
    return when (this) {
        is BookQueryDataResult.Success -> BookQueryResult.Success(
            model = data.toDomain()
        )
        BookQueryDataResult.InvalidSearch -> BookQueryResult.InvalidSearch
        BookQueryDataResult.CursorNotExist -> BookQueryResult.CursorNotExist
        BookQueryDataResult.NotFoundBook -> BookQueryResult.NotFoundBook
    }
}