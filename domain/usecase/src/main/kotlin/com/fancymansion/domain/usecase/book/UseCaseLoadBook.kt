package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
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
    suspend fun loadBookInfo(episodeRef: EpisodeRef): BookInfoModel =
        withContext(dispatcher) {
            bookLocalRepository.loadBookInfo(userId = episodeRef.userId, mode = episodeRef.mode, bookId = episodeRef.bookId)
        }

    suspend fun loadLogic(episodeRef: EpisodeRef): LogicModel =
        withContext(dispatcher) {
            bookLocalRepository.loadLogic(episodeRef)
        }

    suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageModel =
        withContext(dispatcher) {
            bookLocalRepository.loadPage(episodeRef, pageId)
        }

    suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String): File =
        withContext(dispatcher) {
            bookLocalRepository.loadPageImage(episodeRef, imageName)
        }

    suspend fun loadCoverImage(episodeRef: EpisodeRef, imageName: String): File =
        withContext(dispatcher) {
            bookLocalRepository.loadCoverImage(episodeRef.userId, episodeRef.mode, episodeRef.bookId, imageName)
        }
}