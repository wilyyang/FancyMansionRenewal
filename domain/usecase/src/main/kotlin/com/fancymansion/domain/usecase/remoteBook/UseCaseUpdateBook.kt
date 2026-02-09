package com.fancymansion.domain.usecase.remoteBook

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUpdateBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(
        episodeRef: EpisodeRef
    ): Int = withContext(dispatcher) {
        val newVersion =
            bookRemoteRepository.getPublishedBookVersion(publishedId = episodeRef.bookId) + 1

        val bookInfo = bookLocalRepository.loadBookInfo(
            userId = episodeRef.userId,
            mode = episodeRef.mode,
            bookId = episodeRef.bookId
        )
        val episodeInfo = bookLocalRepository.loadEpisodeInfo(episodeRef)

        bookRemoteRepository.uploadBookCoverImage(
            publishedId = episodeRef.bookId,
            episodeRef = episodeRef,
            coverFileName = bookInfo.introduce.coverList.first()
        )

        bookRemoteRepository.uploadBookArchive(
            publishedId = episodeRef.bookId,
            version = newVersion,
            episodeRef = episodeRef
        )
        bookRemoteRepository.updateBookInfo(episodeRef.bookId, bookInfo, episodeInfo, newVersion)
        bookRemoteRepository.pruneEpisodeZipFile(episodeRef.bookId, newVersion)
        newVersion
    }
}