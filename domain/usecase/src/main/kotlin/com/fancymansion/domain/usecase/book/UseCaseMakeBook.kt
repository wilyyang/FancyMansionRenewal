package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.testBookRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.usecase.book.sample.config
import com.fancymansion.domain.usecase.book.sample.content
import com.fancymansion.domain.usecase.book.sample.logic
import com.fancymansion.domain.usecase.book.sample.sampleImageList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseMakeBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun makeSampleBook() =
        withContext(dispatcher) {
            bookLocalRepository.makeBookDir(testBookRef)

            bookLocalRepository.makeConfig(testBookRef, config)
            bookLocalRepository.makeLogic(testBookRef, logic)

            content.pages.forEach {
                bookLocalRepository.makePage(testBookRef, it.id, it)
            }

            sampleImageList.forEach {
                bookLocalRepository.makeImageFromResource(testBookRef, it.first, it.second)
            }
        }
}