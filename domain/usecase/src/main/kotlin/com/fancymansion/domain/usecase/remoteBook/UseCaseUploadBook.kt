package com.fancymansion.domain.usecase.remoteBook

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUploadBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(
        episodeRef: EpisodeRef
    ) :String =
        withContext(dispatcher) {
            // 1. get publishedId
            val publishedId = bookRemoteRepository.getPublishedId()

            // 2. file update
            bookLocalRepository.migrateBookIdToPublishedId(episodeRef, publishedId)

            // 3. load bookInfo, episodeInfo
            val newEpisodeRef = episodeRef.copy(
                bookId = publishedId,
                episodeId = getEpisodeId(publishedId)
            )
            val bookInfo = bookLocalRepository.loadBookInfo(
                userId = newEpisodeRef.userId,
                mode = newEpisodeRef.mode,
                bookId = newEpisodeRef.bookId
            )

            val episodeInfo = bookLocalRepository.loadEpisodeInfo(newEpisodeRef)

            // 4. firebase & fire storage
            bookRemoteRepository.createBookInfo(
                publishedId = publishedId,
                bookInfo = bookInfo,
                episodeInfo = episodeInfo
            )
            bookRemoteRepository.uploadBookArchive(
                publishedId = publishedId,
                episodeRef = newEpisodeRef
            )
            bookRemoteRepository.uploadBookCoverImage(
                publishedId = publishedId,
                episodeRef = newEpisodeRef,
                coverFileName = bookInfo.introduce.coverList.first()
            )

            // 5. return publishedId
            publishedId
        }
}