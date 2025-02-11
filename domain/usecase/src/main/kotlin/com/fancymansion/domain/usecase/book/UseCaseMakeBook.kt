package com.fancymansion.domain.usecase.book

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.baseBookCoverName
import com.fancymansion.core.common.const.imageFileNamePrefix
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.PageModel
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

    suspend fun updatePageContent(episodeRef: EpisodeRef, pageId: Long, page: PageModel) =
        withContext(dispatcher) {
            bookLocalRepository.deletePage(episodeRef, pageId)
            bookLocalRepository.makePage(episodeRef, pageId, page)
        }

    suspend fun makePageImage(episodeRef: EpisodeRef, pageId: Long, uri: Uri) : String =
        withContext(dispatcher) {
            val nextImageNumber = (bookLocalRepository.getPageImageFiles(episodeRef)
                .filter { it.name.startsWith("$pageId") }
                .map {
                    val start = it.name.lastIndexOf("_") + 1
                    val end = it.name.lastIndexOf(".")
                    it.name.substring(start, end).toIntOrNull() ?: 0
                }.sorted().last()) + 1

            val fileExtension = uri.path?.substringAfterLast('.', "")
            val nextImageName = "${pageId}$imageFileNamePrefix$nextImageNumber.$fileExtension"

            makePageImage(episodeRef, nextImageName, uri)
            nextImageName
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

    suspend fun saveEditedPageList(episodeRef: EpisodeRef, editedPageList: List<PageLogicModel>, deleteIds : Set<Long>) =
        withContext(dispatcher) {
            val originLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef)
            val (originDeleteIds, originRemainingIds) = originLogic.logics.map { it.pageId }.toSet().partition { it in deleteIds }

            // Delete PageContent
            originDeleteIds.forEach { pageId ->
                bookLocalRepository.deletePage(episodeRef = episodeRef, pageId = pageId)
            }

            // Add PageContent
            val pagesToAdd = editedPageList.filter { it.pageId !in originRemainingIds }
            pagesToAdd.forEach { page ->
                bookLocalRepository.makePage(
                    episodeRef = episodeRef, pageId = page.pageId,
                    page = PageModel(
                        id = page.pageId,
                        title = page.title,
                        sources = listOf()
                    )
                )
            }

            // Update with the new logic
            originLogic.copy(
                logics = editedPageList
            ).let { updatedLogic ->
                bookLocalRepository.makeLogic(episodeRef = episodeRef, logic = updatedLogic)
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