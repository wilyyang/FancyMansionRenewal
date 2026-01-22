package com.fancymansion.domain.usecase.publishBook

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUploadBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(episodeRef: EpisodeRef, bookInfo: BookInfoModel): Boolean =
        withContext(dispatcher) {
            bookRemoteRepository.createBookInfo(
                episodeRef,
                bookInfo
            ) && bookRemoteRepository.uploadBookArchive(
                episodeRef,
                bookInfo
            ) && bookRemoteRepository.uploadBookCoverImage(episodeRef, bookInfo)
        }
}