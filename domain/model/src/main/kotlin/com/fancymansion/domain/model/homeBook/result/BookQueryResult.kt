package com.fancymansion.domain.model.homeBook.result

import com.fancymansion.domain.model.homeBook.HomeBookItemModel

data class BookQueryModel(
    val bookList: List<HomeBookItemModel>,
    val nextBookIds: List<String>
)

sealed class BookQueryResult {
    data class Success(val model: BookQueryModel) : BookQueryResult()
    sealed class Error : BookQueryResult() {
        object InvalidSearch : Error()
        object CursorNotExist : Error()
        object NotFoundBook : Error()
        object NextBookIdError : Error()
    }
}