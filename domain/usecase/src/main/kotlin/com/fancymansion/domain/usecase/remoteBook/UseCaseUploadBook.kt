package com.fancymansion.domain.usecase.remoteBook

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUploadBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(
        episodeRef: EpisodeRef,
        bookInfo: BookInfoModel,
        episodeInfo: EpisodeInfoModel
    ) =
        withContext(dispatcher) {
            bookRemoteRepository.createBookInfo(
                episodeRef,
                bookInfo,
                episodeInfo
            )
            bookRemoteRepository.uploadBookArchive(
                episodeRef
            )
            bookRemoteRepository.uploadBookCoverImage(
                episodeRef,
                bookInfo.introduce.coverList.first()
            )
        }
}