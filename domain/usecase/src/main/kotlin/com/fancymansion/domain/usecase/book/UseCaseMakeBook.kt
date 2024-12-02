package com.fancymansion.domain.usecase.book

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.baseBookCoverName
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

    suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String) =
        withContext(dispatcher) {
            bookLocalRepository.deletePageImage(episodeRef, imageName)
        }

    suspend fun createBookCover(episodeRef: EpisodeRef, bookInfo: BookInfoModel, uri: Uri) =
        withContext(dispatcher) {
            baseBookCoverName(episodeRef.bookId, 1).let { newBookCoverName ->
                bookLocalRepository.makeBookInfo(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    bookInfo.copy(introduce = bookInfo.introduce.copy(coverList = listOf(newBookCoverName)))
                ) && bookLocalRepository.makeCoverImageFromUri(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    newBookCoverName, uri
                )
            }
        }

    suspend fun removeBookCover(episodeRef: EpisodeRef, bookInfo: BookInfoModel) =
        withContext(dispatcher) {
            bookInfo.introduce.coverList.getOrNull(0)?.let { coverImageName ->
                bookLocalRepository.makeBookInfo(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    bookInfo.copy(introduce = bookInfo.introduce.copy(coverList = listOf()))
                ) && bookLocalRepository.deleteCoverImage(episodeRef.userId, episodeRef.mode, episodeRef.bookId, coverImageName)
            }?: true
        }

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef = testEpisodeRef) =
        withContext(dispatcher) {
            bookLocalRepository.makeSampleEpisode(episodeRef)
        }
}