package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Page
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseLoadBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun loadLogic(bookRef: BookRef): Logic =
        withContext(dispatcher) {
            bookLocalRepository.loadLogicFromFile(bookRef)
        }

    suspend fun loadPage(bookRef: BookRef, pageId: Long): Page =
        withContext(dispatcher) {
            bookLocalRepository.loadPageFromFile(bookRef, pageId)

        }
}