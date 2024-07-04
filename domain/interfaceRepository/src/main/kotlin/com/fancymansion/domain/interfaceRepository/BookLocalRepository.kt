package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel

interface BookLocalRepository {

    suspend fun loadLogicFromFile(bookRef: BookRef): LogicModel
    suspend fun loadPageFromFile(bookRef: BookRef, pageId: Long): PageModel
    suspend fun makeSampleBookFile()

    suspend fun deleteBookActionCount(bookRef: BookRef)
    suspend fun incrementActionCount(bookRef: BookRef, actionId: Long)
    suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int
}