package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Page

interface BookLocalRepository {

    suspend fun loadLogicFromFile(bookRef: BookRef): Logic
    suspend fun loadPageFromFile(bookRef: BookRef, pageId: Long): Page

    suspend fun deleteBookActionCount(bookRef: BookRef)
    suspend fun incrementActionCount(bookRef: BookRef, actionId: Long)
    suspend fun getActionCount(bookRef: BookRef, actionId: Long) : Int
}