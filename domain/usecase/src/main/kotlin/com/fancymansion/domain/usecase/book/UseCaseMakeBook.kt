package com.fancymansion.domain.usecase.book

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseMakeBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun makeBookInfo(episodeRef: EpisodeRef, bookInfo : BookInfoModel) =
        withContext(dispatcher) {
            bookLocalRepository.makeBookInfo(userId = episodeRef.userId, mode = episodeRef.mode, bookId = episodeRef.bookId, bookInfo = bookInfo)
        }

    suspend fun makePageImage(episodeRef: EpisodeRef, imageName: String, uri: Uri) =
        withContext(dispatcher) {
            bookLocalRepository.makePageImageFromUri(episodeRef, imageName, uri)
        }

    suspend fun makeCoverImage(episodeRef: EpisodeRef, imageName: String, uri: Uri) =
        withContext(dispatcher) {
            bookLocalRepository.makeCoverImageFromUri(episodeRef.userId, episodeRef.mode, episodeRef.bookId, imageName, uri)
        }

    suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String) =
        withContext(dispatcher) {
            bookLocalRepository.deletePageImage(episodeRef, imageName)
        }

    suspend fun deleteCoverImage(episodeRef: EpisodeRef, imageName: String) =
        withContext(dispatcher) {
            bookLocalRepository.deleteCoverImage(episodeRef.userId, episodeRef.mode, episodeRef.bookId, imageName)
        }

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef = testEpisodeRef) =
        withContext(dispatcher) {
            bookLocalRepository.makeSampleEpisode(episodeRef)
        }
}