package com.fancymansion.domain.model.homeBook.result

import com.fancymansion.domain.model.homeBook.HomeBookItemModel

data class BookQueryModel(
    val bookList: List<HomeBookItemModel>,
    val nextBookIds: List<String>
)

sealed class BookQueryResult {
    data class Success(val model: BookQueryModel) : BookQueryResult()
    object InvalidSearch : BookQueryResult()
    object CursorNotExist : BookQueryResult()
    object NotFoundBook : BookQueryResult()
    object NextBookIdError : BookQueryResult()
}