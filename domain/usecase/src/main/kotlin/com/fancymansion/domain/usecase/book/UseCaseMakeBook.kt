package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.usecase.book.sample.bookInfo
import com.fancymansion.domain.usecase.book.sample.content
import com.fancymansion.domain.usecase.book.sample.episodeInfo
import com.fancymansion.domain.usecase.book.sample.logic
import com.fancymansion.domain.usecase.book.sample.sampleImageList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseMakeBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun makeSampleEpisode() =
        withContext(dispatcher) {
            bookLocalRepository.makeBookDir(
                userId = testEpisodeRef.userId,
                mode = testEpisodeRef.mode,
                bookId = testEpisodeRef.bookId
            )
            bookLocalRepository.makeBookInfo(
                userId = testEpisodeRef.userId,
                mode = testEpisodeRef.mode,
                bookId = testEpisodeRef.bookId,
                bookInfo = bookInfo
            )

            bookLocalRepository.makeEpisodeDir(
                episodeRef = testEpisodeRef
            )

            bookLocalRepository.makeEpisodeInfo(
                episodeRef = testEpisodeRef,
                episodeInfo = episodeInfo
            )

            bookLocalRepository.makeLogic(testEpisodeRef, logic)

            content.pages.forEach {
                bookLocalRepository.makePage(testEpisodeRef, it.id, it)
            }

            sampleImageList.forEach {
                bookLocalRepository.makePageImageFromResource(testEpisodeRef, it.first, it.second)
            }
        }
}