package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UseCaseLoadBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun loadLogic(bookRef: BookRef): LogicModel =
        withContext(dispatcher) {
            bookLocalRepository.loadLogic(bookRef)
        }

    suspend fun loadPage(bookRef: BookRef, pageId: Long): PageModel =
        withContext(dispatcher) {
            bookLocalRepository.loadPage(bookRef, pageId)
        }

    suspend fun loadImage(bookRef: BookRef, imageName: String): File =
        withContext(dispatcher) {
            bookLocalRepository.loadImage(bookRef, imageName)
        }
}