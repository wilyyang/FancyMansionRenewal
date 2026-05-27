package com.fancymansion.data.datasource.firebase.database.book.model.result

import com.fancymansion.data.datasource.firebase.database.book.model.HomeBookItemData

sealed class BookQueryDataResult {
    data class Success(val bookList: List<HomeBookItemData>) : BookQueryDataResult()
    object InvalidSearch : BookQueryDataResult()
    object CursorNotExist : BookQueryDataResult()
    object NotFoundBook : BookQueryDataResult()
}

sealed class NextBookIdDataResult {
    data class Success(val nextBookIds: List<String>) : NextBookIdDataResult()
    object InvalidSearch : NextBookIdDataResult()
    object CursorNotExist : NextBookIdDataResult()
    object NotFoundBook : NextBookIdDataResult()
}